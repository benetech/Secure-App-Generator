function xformSelectionChange() {
	this.console.log("Checking xFormSelection");
	var xFormList = this.document.getElementById('defaultXFormSelection');
	var index = xFormList.selectedIndex;	
	var nextButtonEnabled = false;
	var uploadForm = this.document.getElementById('uploadCustomXFormButton');
	if(index == 0)
	{
		nextButtonEnabled = false;
	}
	else
	{
		nextButtonEnabled = true;
		uploadForm.value = null;
	}

	updateNextButton(nextButtonEnabled);
}

function uploadxFormSelected() {
	this.console.log("Checking Upload xFormSelection");
	var xFormList = this.document.getElementById('defaultXFormSelection').selectedIndex = 0;
	
	var nextButtonEnabled = false;
	var uploadForm = this.document.getElementById('uploadCustomXFormButton');
	if(uploadForm.value != null && uploadForm.value != "")
	{
		nextButtonEnabled = true;
	}
	updateNextButton(nextButtonEnabled);
}

function updateNextButton(nextButtonEnabled)
{
	var nextButton = this.document.getElementById("next")
	//FIXME ideally should use button_next_disabled and button_next but wouldn't work
	if(nextButtonEnabled)
	{
		nextButton.disabled = false;
		nextButton.style.backgroundColor = "yellow";
		nextButton.style.color = "black";
	}
	else
	{
		nextButton.disabled = true;
		nextButton.style.backgroundColor = "gray";
		nextButton.style.color = "lightgray";
	}
}