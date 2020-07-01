jQuery.noConflict();

function initializationCheckbox(parentDiv,requestBasicUrl,parameterName){
	var checkboxDiv = parentDiv.find('#checkbox_div');
	var messageD = parentDiv.find('#result_message');
	var checkbox_request = requestBasicUrl+'fillCheckboxItems?name='+parameterName;
	jQuery.ajax({
				type: "GET",
				url: checkbox_request,
				contentType: "application/json; charset=utf-8",
				success: function(result){
				for(i=0;i<result.list.length;i++){
					checkboxDiv.append("<label style='padding:0 0 0 10px'><input type='checkbox' name='checkbox_" + result.list[i].value  + "'>" + result.list[i].name + "</input></label>");
				}
					messageD.text(result.message);
				}});
}