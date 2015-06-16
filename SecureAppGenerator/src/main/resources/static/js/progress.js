function showProgress() {
    	  this.document.myform.next.innerHTML = 'Building';
    	  return true;
    	}


function showAlert()
	 	{
	this.document.body.style.cursor = 'wait';
		this.document.getElementById("progressArea").innerHTML = "Building!";
			alert('Hello from JavaScript!');
	 	}

function showImage()
{
	this.document.getElementById("progressBar").style.display = 'block';
	this.document.getElementById("progressArea").innerHTML = "Building!";
	this.document.body.style.cursor = 'wait';
	setTimeout(function(){
		alert('Hello from JavaScript!');
	}, 2000);	

	}

function startTimer(duration, display) {
    var timer = duration, minutes, seconds;
    setInterval(function () {
        minutes = parseInt(timer / 60, 10);
        seconds = parseInt(timer % 60, 10);

        minutes = minutes < 10 ? "0" + minutes : minutes;
        seconds = seconds < 10 ? "0" + seconds : seconds;

        display.textContent = minutes + ":" + seconds;

        if (--timer < 0) {
            timer = duration;
        }
    }, 1000);
}

function startNewTimer() {
    var twoMinutes = 60 * 2,
        display = this.document.getElementById("progressArea");
    startTimer(twoMinutes, display);
};
