Ext.define('Ssp.view.ToolsMenu', {
	extend: 'Ext.grid.Panel',
	alias : 'widget.ToolsMenu',
    mixins: [ 'Deft.mixin.Injectable',
              'Deft.mixin.Controllable'],
    controller: 'Ssp.controller.ToolsViewController',
    inject: {
        toolsStore: 'toolsStore'
    },
    initComponent: function(){
    	Ext.apply(this,
    			   {
		    		width: '100%',
		    		height: '100%',
    				store: this.toolsStore,
    				columns:[{
    				           header: "Assigned Tools", 
    				           dataIndex: "name", 
    				           flex:1 }]
		    	    });
    	
    	return this.callParent(arguments);
    }
});