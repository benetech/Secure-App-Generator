function showAlert()
{
	alert('Hello from JavaScript!');
}

function initalize()
{
	this.document.getElementById('cellphone_center_title').innerHTML = "";
	this.document.getElementById('cellphone_center_data').innerHTML = "";
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


var myProgressBar = null
var timerId = null

function loadProgressBar() {
	myProgressBar = new ProgressBar("my_progress_bar_1",{
		borderRadius: 10,
		width: 300,
		height: 20,
		maxValue: 100,
		labelText: "Loaded in {value,0} %",
		orientation: ProgressBar.Orientation.Horizontal,
		direction: ProgressBar.Direction.LeftToRight,
		animationStyle: ProgressBar.AnimationStyle.LeftToRight1,
		animationSpeed: 1.5,
		imageUrl: 'images/v_fg12.png',
		backgroundUrl: 'images/h_bg2.png',
		markerUrl: 'images/marker2.png'
	});
	
	timerId = window.setInterval(function() {
		if (myProgressBar.value >= myProgressBar.maxValue)
			myProgressBar.setValue(0);
		else
			myProgressBar.setValue(myProgressBar.value+1);
		
	},
	100);
}

function showProgress(buildText)
{
	initalize();
	var myCanvas = this.document.getElementById('ProgressArea');
	
	var percentComplete = 0;
	
	var circle = new ProgressCircle({
	    canvas: myCanvas, 
	    minRadius: 100, // Inner radius of the innermost circle
	    arcWidth: 20, // Width of each circle
	    centerX: 270, // X coordinate of the circle center
	    centerY: 140, // Y coordinate of the circle center
	    infoLineLength: 100, // Length of the info line
        horizLineLength: -30, // Length of the horizontal info line
        infoLineBaseAngle: 130.4,
        });
	
	
	circle.addEntry({
	    fillColor: '#6ECDE4',
	    progressListener: function() {
	    	percentComplete = percentComplete + .01;
	    	if(percentComplete > 1)
	    		return 1;
	    	return percentComplete;},
	    	infoListener: function() {return buildText;},// (Optional)	    	
	 }); 		
	circle.start(500); 
	this.document.forms["buildApp"].submit();
	return true;
}

