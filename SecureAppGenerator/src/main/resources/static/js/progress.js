function showAlert()
{
	alert('Hello from JavaScript!');
}

function showBuildingProgressBar()
{
//	this.document.getElementById("progressBar").style.display = 'block';
	this.document.getElementById("next").disabled = true;
	this.document.getElementById("next").style.visibility = "hidden";
	this.document.getElementById("previous").disabled = true;
	this.document.getElementById("previous").style.visibility = "hidden";
	this.document.body.style.cursor = 'wait';
}

function startTimer(duration, display) {
    var timer = duration, minutes, seconds;
    setInterval(function () {
        minutes = parseInt(timer / 60, 10);
        seconds = parseInt(timer % 60, 10);

        minutes = minutes < 10 ? "0" + minutes : minutes;
        seconds = seconds < 10 ? "0" + seconds : seconds;

        display.textContent = "Building APK: " + minutes + ":" + seconds;

        if (--timer < 0) {
            timer = duration;
        }
    }, 1000);
}

function startNewTimer() {
	showBuildingProgressBar();
	
    var twoMinutes = 60 * 2,
    		display = this.document.getElementById("progressArea");
    startTimer(twoMinutes, display);
};
