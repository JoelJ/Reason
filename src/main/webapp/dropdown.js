DROPDOWN = {
	init: function() {
		Event.observe(window, 'load', function() {
			$$('.dropdown>select').each(function(it) {
				it.observe('change', DROPDOWN.onDropdownChanged);
				console.log(it);
			});
		});
	},

	onDropdownChanged: function(event) {
		var select = event.target;
		DROPDOWN.dropdownChanged(select);
	},

	dropdownChanged: function(select) {
		var selectedIndex = select.selectedIndex;
		var selected = $(select[selectedIndex]);
		var previousIndex = select.getAttribute("previous");
		var previous = $(select[previousIndex]);
		select.setAttribute("previous", selectedIndex);

		console.log(previous);

		var oldId = select.getAttribute('id') + "-" + previous.value;
		var oldDiv = $(oldId);

		var newId = select.getAttribute('id') + "-" + selected.value;
		var newDiv = $(newId);

		if(oldDiv) {
			var nestedSelects = $$("#" + oldId + " select");
			if(nestedSelects) {
				nestedSelects.each(function(it) {
					it.selectedIndex = 0;
					it.setAttribute('previous', 0);
				});
			}

			$$("#"+oldId + " div.dropdown").each(function(it) {
				it.addClassName('hidden');
			});
			oldDiv.addClassName('hidden');
		}

		if(newDiv) {
			newDiv.removeClassName('hidden');
		}
	}
};

