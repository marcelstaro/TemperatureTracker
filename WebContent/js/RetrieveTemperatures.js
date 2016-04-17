
$(document).ready(function(){
    $("button").click(function(){
        $.post("https://temperature-tracker.herokuapp.com//Temperature.json",
        {
          username: $("#username").val(),
          password: $("#password").val()
        },
        function(data,status){
        	
        	$.each(data.temperature, function(index, element) {
        		$("#result").append('<p>');
        		$("#result").append('</br>id test: ' + element.id);
        		$("#result").append('</br>Temperature: ' + element.temp);
        		$("#result").append('</br>Date: ' + element.date);
        		$("#result").append('</p>');
            });         
        },"json");
    });
});
