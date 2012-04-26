(function() {
  var $, context;

  $ = jQuery;

  context = window.context || (window.context = {});

  context.sessionService || (context.sessionService = new mygps.service.SessionService("../api/1/session"));

  context.messageService || (context.messageService = new mygps.service.MessageService("../api/1/mygps/message"));

  context.session || (context.session = new mygps.session.Session(context.sessionService));

  $('#contact-page').live('pagecreate', function() {
    var contactPage, viewModel;
    contactPage = this;
    viewModel = new mygps.viewmodel.ContactViewModel(context.session, context.messageService);
    window.viewModel = viewModel;
    $("body").loadTemplates({
      bannerTemplate: "/ssp/MyGPS/templates/banner.html",
      footerTemplate: "/ssp/MyGPS/templates/footer.html"
    }).done(function() {
      ko.applyBindings(viewModel, contactPage);
    });
    $('#contact-page').live('pagebeforeshow', function() {
      window.viewModel = viewModel;
      viewModel.load();
    });
  });

}).call(this);
