jQuery.noConflict();

function initializationProtocol(parent){
	var selectP = parent.find('#ProtocolList');
	var remote = parent.find('#remote');
    var input = parent.find('#input');
	var select = selectP.val();
	console.log(selectP)
	alert(select)
	alert(selectP.find("option:selected").text())
	alert(selectP)
	if(select == 'INPUT'){remote.hide();input.show();}else{remote.show();input.hide();}
}

function bindProtocolOnChange(parent){
	var selectP = parent.find('#ProtocolList');
	var remote = parent.find('#remote');
	var input = parent.find('#input');
	selectP.change(function(){
		var select = jQuery(this).children('option:selected').val();
		alert(select)
		if(select == 'INPUT'){remote.hide();input.show();}else{remote.show();input.hide();}
		}
	)
}