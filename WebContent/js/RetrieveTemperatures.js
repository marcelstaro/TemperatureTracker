
$(document).ready(function(){
	$('#header').load('header.html');
	
    $('button').click(function(){
        $.post("https://temperature-tracker.herokuapp.com/Temperature.json",
        {
          username: $("#username").val(),
          password: $("#password").val()
        },
        function(response,status){        	
        	if ((typeof response == 'string') && (response.indexOf('Failed') > -1)) {
        		alert(response);
        	} else {       		
        		localStorage.setItem("readings", JSON.stringify(response.temperature));
        		window.location.replace('readings-chart.html');
        	}
        });
    });
});
