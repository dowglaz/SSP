(function() {
  var $, context;

  $ = jQuery;

  context = window.context || (window.context = {});

  context.sessionService || (context.sessionService = new mygps.service.SessionService("../api/1/session"));

  context.taskService || (context.taskService = new mygps.service.TaskService("../api/1/mygps/task"));

  context.session || (context.session = new mygps.session.Session(context.sessionService));

  $('#home-page').live('pagecreate', function(event) {
    var homePage, viewModel;
    homePage = this;
    viewModel = new mygps.viewmodel.HomeViewModel(context.session, context.taskService);
    window.viewModel = viewModel;
    $("body").loadTemplates({
      bannerTemplate: "/ssp/MyGPS/templates/banner.html",
      footerTemplate: "/ssp/MyGPS/templates/footer.html",
      tasksTemplate: "/ssp/MyGPS/templates/tasks.html",
      taskTemplate: "/ssp/MyGPS/templates/task.html",
      taskDetailTemplate: "/ssp/MyGPS/templates/taskdetail.html"
    }).done(function() {
      ko.applyBindings(viewModel, homePage);
    });
    $('#home-page').live('pagebeforeshow', function() {
      window.viewModel = viewModel;
      viewModel.load();
    });
  });

}).call(this);
