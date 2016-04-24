var listDays = new Array();
var dayIndex = 0;
$(document).ready(function(){
	$('#header').load('header.html');
	
	var readings = localStorage.getItem("readings");
	
	var readingsMap = buildMap(readings);
	
	var day = listDays[dayIndex];
	
	displayChart(getDataPointsForDay(readingsMap[day]), "Readings for Date " + day.replace(/-/g,"/"));
	
	$('#download-btn').click(function(){
		JSONToCSVConvertor(readings,"Readings", true); 
	});
	
	$('#logout-btn').click(function(){
		window.location.replace('index.html');
	});
	
	$('#prevday-btn').click(function(){
		if (dayIndex > 0) {
			var day = listDays[--dayIndex];
			displayChart(getDataPointsForDay(readingsMap[day]), "Readings for Date " + day.replace(/-/g,"/"));
		}
	});
	$('#nextday-btn').click(function(){
		if (dayIndex < listDays.length) {
			var day = listDays[++dayIndex];
			displayChart(getDataPointsForDay(readingsMap[day]), "Readings for Date " + day.replace(/-/g,"/"));
		}
	});
});

function buildMap(JSONData) {
	var arrData = typeof JSONData != 'object' ? JSON.parse(JSONData) : JSONData;
	var map = {};
	for (var i = 0; i < arrData.length; i++) {	
		var res = arrData[i].date.split(" ");
		var day = res[0];
		var hour = res[1];
		
		if (day != null || arrData[i].temp != null) {
			if (map[day] == null) {
				map[day] = new Array();
			}
			
			map[day].push({
				temp: arrData[i].temp,
				hour: hour
			});
			
			if (!($.inArray(day, listDays) > -1)) {
				listDays.push(day);
			}
			
		}		
		
	}
	
	dayIndex = listDays.length - 1;
	
	return map;
}

function getDataPointsForDay(readingsOfDay) {
	var datapoints = new Array();
	
	for (var i = 0; i < readingsOfDay.length; i++) {
		datapoints.push({
			label: readingsOfDay[i].hour.slice(1,8),
			//x : readingsOfDay[i].hour,//parseInt(hmsToSecondsOnly(readingsOfDay[i].hour)),//.parseInt(),
			y : parseFloat(readingsOfDay[i].temp)//.parseFloat()
		});
	}
	
	return datapoints;
}

function displayChart(dataPoints, name) {	
	for (var i = 0; i < dataPoints.length; i++) {
		console.log("data point " + i + " : " + dataPoints[i].label);
	}
	
	var chart = new CanvasJS.Chart("chart",
	{
		animationEnabled: true,
		title:{text: name},
		axisX: { maximum: 100 , title: 'Hour'},
		axisY: { maximum: 100 , title: 'Temperature (F)'},
		data: [
		{
			type: "spline", //change type to bar, line, area, pie, etc
			showInLegend: true,
			name:"Readings",
			dataPoints: dataPoints	
		}
		],
		legend: {
			cursor: "pointer",
			itemclick: function (e) {
				if (typeof(e.dataSeries.visible) === "undefined" || e.dataSeries.visible) {
					e.dataSeries.visible = false;
				} else {
					e.dataSeries.visible = true;
			}
			chart.render();
			}
		}
	});

	chart.render();	
}


function JSONToCSVConvertor(JSONData, ReportTitle, ShowLabel) {
    //If JSONData is not an object then JSON.parse will parse the JSON string in an Object
    var arrData = typeof JSONData != 'object' ? JSON.parse(JSONData) : JSONData;
    
    var CSV = '';    
    //Set Report title in first row or line
    
    CSV += ReportTitle + '\r\n\n';

    //This condition will generate the Label/Header
    if (ShowLabel) {
        var row = "";
        
        //This loop will extract the label from 1st index of on array
        for (var index in arrData[0]) {
            
            //Now convert each value to string and comma-seprated
            row += index + ',';
        }

        row = row.slice(0, -1);
        
        //append Label row with line break
        CSV += row + '\r\n';
    }
    
    //1st loop is to extract each row
    for (var i = 0; i < arrData.length; i++) {
        var row = "";
        
        //2nd loop will extract each column and convert it in string comma-seprated
        for (var index in arrData[i]) {
            row += '"' + arrData[i][index] + '",';
        }

        row.slice(0, row.length - 1);
        
        //add a line break after each row
        CSV += row + '\r\n';
    }

    if (CSV == '') {        
        alert("Invalid data");
        return;
    }   
    
    //Generate a file name
    var fileName = "Report_";
    //this will remove the blank-spaces from the title and replace it with an underscore
    fileName += ReportTitle.replace(/ /g,"_");   
    
    //Initialize file format you want csv or xls
    var uri = 'data:text/csv;charset=utf-8,' + encodeURI(CSV);
    
    // Now the little tricky part.
    // you can use either>> window.open(uri);
    // but this will not work in some browsers
    // or you will not get the correct file extension    
    
    //this trick will generate a temp <a /> tag
    var link = document.createElement("a");    
    link.href = uri;
    
    //set the visibility hidden so it will not effect on your web-layout
    link.style = "visibility:hidden";
    link.download = fileName + ".csv";
    
    //this part will append the anchor tag and remove it after automatic click
    document.body.appendChild(link);
    link.click();
    document.body.removeChild(link);
}

