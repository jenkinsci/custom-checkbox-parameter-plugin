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
					checkboxDiv.append("<label style='padding:0 0 0 10px'><input type='checkbox' " + result.list[i].checked + " name='checkbox_" + result.list[i].value  + "'>" + result.list[i].name + "</input></label>");
				}
					messageD.text(result.message);
				}});

	var checkbox_all = parentDiv.find('.checkbox-all');
	checkbox_all.bind('click', function(){
		var checkbox_all_checked = this.checked
		checkboxDiv.find(":checkbox").each(function(index, item){
		item.checked = checkbox_all_checked;})
	});
}