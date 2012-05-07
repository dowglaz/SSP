Ext.define('Ssp.store.Tools', {
    extend: 'Ext.data.Store',
    model: 'Ssp.model.Tool',
    autoLoad: false,
    constructor: function(){
		return this.callParent(arguments);
    },
    data: [{ name: "Profile", toolType: "Profile" },
           { name: "Student Intake", toolType: "StudentIntake" },
           { name: "Action Plan", toolType: "ActionPlan" },
           { name: "Journal", toolType: "Journal" },
           { name: "Early Alert", toolType: "EarlyAlert" }]
});