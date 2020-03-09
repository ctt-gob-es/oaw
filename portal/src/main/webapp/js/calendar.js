var param_uno;
var param_dos;
var formato;
var calendarWindow;
var urlstring = '/oaw/html/calendar.html';



function popUpCalendar(ctl, ctl2, format){

	param_uno = ctl;
	param_dos = ctl2;
	formato = format;
	posicionx = findPosX(ctl);
	posiciony = findPosY(ctl);
	posiciony += 125;

posiciony -= document.body.scrollTop;

	if(screen.width >1025 && posicionx > 900){
	posicionx -= 100;}
	if(screen.width <1025 && posicionx > 900){
	posicionx-=225;} 
posicionx -= document.body.scrollLeft;	

//comprobar si ya hay una instancia de calendarWindow
if(calendarWindow){calendarWindow.close();}
	
	//if (!calendarWindow.closed && calendarWindow.location){
	//calendarWindow.close();
	calendarWindow= window.open(urlstring,'calendario','top='+ posiciony +',left='+ posicionx +',height=190,width=245,toolbar=no,minimize=no,status=no,memubar=no,location=no,scrollbars=no,resize=no')	
	//}else{
	//calendarWindow = window.open(urlstring,'calendario','top='+ posiciony +',left='+ posicionx +',height=190,width=245,toolbar=no,minimize=no,status=no,memubar=no,location=no,scrollbars=no')
	//}

	if (window.focus) {calendarWindow.focus();}
}
//posicion de los eventos para colocar las ventanas de popUp
function findPosX(obj)
{

var curleft = 0;
	if (obj.offsetParent)
	{
		while (obj.offsetParent)
		{
			curleft += obj.offsetLeft
			obj = obj.offsetParent;
		}
	}
	else if (obj.x)
		curleft += obj.x;
	return curleft;
}
function findPosY(obj)
{
var curtop = 0;
	if (obj.offsetParent)
	{
		while (obj.offsetParent)
		{
			curtop += obj.offsetTop
			obj = obj.offsetParent;
		}
	}
	else if (obj.y)
		curtop += obj.y;
	
	curtop -= document.documentElement.scrollTop;
	return curtop;
}




/*
function generateCalendarFrom(count,src,buttonOnclick,altLink) {
	var content=document.getElementById("fromDate"+count);
		boton=document.createElement("img");
		boton.src=src;
		var miEnlace=document.createElement("a");
		miEnlace.className="imgCalendar";
		miEnlace.alt=altLink;
		miEnlace.href="javascript://;";
		miEnlace.onclick=function() { eval(buttonOnclick);}
		miEnlace.appendChild(boton);
		content.appendChild(miEnlace);
		
}

function generateCalendarTo(count,src,buttonOnclick,altLink) {
	var content=document.getElementById("toDate"+count);
		boton=document.createElement("img");
		boton.src=src;
		var miEnlace=document.createElement("a");
		miEnlace.className="imgCalendar";
		miEnlace.alt=altLink;
		miEnlace.href="javascript://;";
		miEnlace.onclick=function() { eval(buttonOnclick);}
		miEnlace.appendChild(boton);
		content.appendChild(miEnlace);
		
}*/
