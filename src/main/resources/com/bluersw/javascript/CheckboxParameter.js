Behaviour.specify(".custom-checkbox-parameter", "custom-checkbox-parameter", 0, function (container) {
	const checkboxItems = container.querySelector(".checkbox-items");
	const message = container.querySelector(".checkbox-result-message");
	const selectAll = container.querySelector(".checkbox-all");
	const requestUrl = new URL(container.dataset.checkboxUrl, window.location.href);
	requestUrl.searchParams.set("name", container.dataset.parameterName);

	fetch(requestUrl, {headers: {Accept: "application/json"}})
		.then(function (response) {
			if (!response.ok) {
				throw new Error(response.statusText);
			}
			return response.json();
		})
		.then(function (result) {
			result.list.forEach(function (item) {
				const label = document.createElement("label");
				const checkbox = document.createElement("input");
				label.style.cssText = "padding:10px;float:left;";
				checkbox.type = "checkbox";
				checkbox.name = "checkbox_" + item.value;
				checkbox.checked = item.checked === "checked";
				label.appendChild(checkbox);
				label.appendChild(document.createTextNode(item.name));
				checkboxItems.appendChild(label);
			});
			message.textContent = result.message;
		})
		.catch(function (error) {
			message.textContent = error.message;
		});

	selectAll.addEventListener("click", function () {
		checkboxItems.querySelectorAll("input[type='checkbox']").forEach(function (checkbox) {
			checkbox.checked = selectAll.checked;
		});
	});
});
