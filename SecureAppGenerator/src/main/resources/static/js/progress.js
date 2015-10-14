
function initalize()
{
	this.document.getElementById('cellphone_center_title').innerHTML = "";
	this.document.getElementById('cellphone_center_data').innerHTML = "";
	this.document.body.style.cursor = 'wait';
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
		circle.start(1000); 
	//	this.document.forms["startBuildingApp"].submit();
	return true;
}

