(function() {
  var $, context;

  $ = jQuery;

  context = window.context || (window.context = {});

  context.sessionService || (context.sessionService = new mygps.service.SessionService("../api/1/session"));

  context.studentIntakeService || (context.studentIntakeService = new mygps.service.StudentIntakeService("../api/1/mygps/intake"));

  context.session || (context.session = new mygps.session.Session(context.sessionService));

  $('#intake-page').live('pagecreate', function(event) {
    var intakePage, viewModel;
    intakePage = this;
    viewModel = new mygps.viewmodel.StudentIntakeViewModel(context.session, context.studentIntakeService);
    window.viewModel = viewModel;
    $("body").loadTemplates({
      bannerTemplate: "/ssp/MyGPS/templates/banner.html",
      footerTemplate: "/ssp/MyGPS/templates/footer.html",
      sectionTemplate: "/ssp/MyGPS/templates/form/section.html",
      agreementTemplate: "/ssp/MyGPS/templates/form/question/agreement.html",
      textareaTemplate: "/ssp/MyGPS/templates/form/question/textarea.html",
      textinputTemplate: "/ssp/MyGPS/templates/form/question/textinput.html",
      selectTemplate: "/ssp/MyGPS/templates/form/question/select.html",
      checklistTemplate: "/ssp/MyGPS/templates/form/question/checklist.html",
      radiolistTemplate: "/ssp/MyGPS/templates/form/question/radiolist.html",
      selectOptionTemplate: "/ssp/MyGPS/templates/form/question/option/select.html",
      checkOptionTemplate: "/ssp/MyGPS/templates/form/question/option/check.html",
      radioOptionTemplate: "/ssp/MyGPS/templates/form/question/option/radio.html"
    }).done(function() {
      ko.applyBindings(viewModel, intakePage);
    });
    $('#intake-page').live('pagebeforeshow', function() {
      window.viewModel = viewModel;
      viewModel.load();
    });
  });

}).call(this);
