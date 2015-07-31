function xformSelectionChange() {
	var list = this.document.getElementById('defaultXFormSelection');
	var index = list.selectedIndex;	
	var nextButton = this.document.getElementById("next")
	//if(index == 0)
	//	nextButton.disabled = true;
	//else
		nextButton.disabled = false;
		
	//var uploadForm = this.document.getElementById('uploadCustomXFormButton');
	
	
}