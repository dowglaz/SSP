/*
 * Licensed to Apereo under one or more contributor license
 * agreements. See the NOTICE file distributed with this work
 * for additional information regarding copyright ownership.
 * Apereo licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file
 * except in compliance with the License.  You may obtain a
 * copy of the License at the following location:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
Ext.define('Ssp.controller.admin.config.MessageQueueDisplayAdminViewController', {
    extend: 'Deft.mvc.ViewController',
    mixins: [ 'Deft.mixin.Injectable' ],
    inject: {
    	apiProperties: 'apiProperties',
    	store: 'messageQueueStore',
    	unpagedStore: 'messageQueueStore',
    	formUtils: 'formRendererUtils',
		adminSelectedIndex: 'adminSelectedIndex',
		storeUtils:'storeUtils'
    },
    config: {
    	containerToLoadInto: 'adminforms',
    	formToDisplay: 'messagequeuedetails'
    },
    control: {
    	'detailsButton': {
			click: 'onDetailsClick'
		}
    },       
	init: function() {
		var me=this;
		//
		// var params = {store:me.store,
		// 		unpagedStore:me.unpagedStore,
		// 		propertyName:"name",
		// 		grid:me.getView(),
		// 		model:null,
		// 		selectedIndex: me.adminSelectedIndex};
		// me.storeUtils.onStoreUpdate(params);
		
		return me.callParent(arguments);
    },

    onDetailsClick: function(button) {
		var me = this;
   		var grid, record, idx;
   		grid = button.up('grid');
   		record = grid.getView().getSelectionModel().getSelection()[0];

   		this.adminSelectedIndex.set('value', -1);

        if (record) {
            var model = new  Ext.create('Ssp.model.Message');
            model.populateFromGenericObject(record.data);

            if ( me.messageQueuePopup ) {
                me.messageQueuePopup.destroy();
            }

            me.messageQueuePopup = Ext.create('Ssp.view.admin.forms.config.MessageTemplatePreview', {
                messageQueuePopup: model
            });

            me.messageQueuePopup.show();
        }else{
            Ext.Msg.alert('SSP Error', 'Please select an message for more details.');
        }
   	},

	destroy: function() {
		var me=this;
		if ( me.messageQueuePopup ) {
			me.messageQueuePopup.destroy();
		}
		return me.callParent( arguments );
	}
});
