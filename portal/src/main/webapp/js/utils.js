window.onload = init;

function init() {
	showCalendar();
	if (document.getElementById("cartucho") != null){
		checkCartridge('normaDiv', 'enlacesRotos', document.getElementById("cartucho").value);
	}
	if (document.getElementById("cartucho4") != null){
		checkCartridge('normaDiv', 'enlacesRotos', document.getElementById("cartucho4").value);
	}
}

function showCalendar() {
	if (document.getElementById("calendar")!= null){
		document.getElementById("calendar").style.display = 'inline';
	}
}

function escBarra(evento,campo,tipo) {
	var miEvento = evento ? evento : window.event;
	var tecla = miEvento.keyCode;
	if(miEvento.shiftKey && (tecla>=35 && tecla<=36)) return true;
	if(miEvento.ctrlKey && tecla==67) return true;
	if(miEvento.ctrlKey && tecla==86) return true;
	if(miEvento.ctrlKey && tecla==88) return true;
	if(miEvento.shiftKey || miEvento.ctrlKey || miEvento.altKey) return false; 
    var correcto=false;
    var modificar = 0;
    var valor = campo.value;
	
	if (tecla==8 || tecla==39 || tecla==37|| tecla==46 || tecla==9 ) return true;
	if ((tecla>=48 && tecla<=57) || (tecla>=96 && tecla<=105) ) correcto=true; 
    else return false; 
			
	if(correcto)
	{	
			switch (tipo) {
				case 1: caracter = '/'; break;
				case 2: caracter = ':'; break;
				case 3: caracter = '/'; break;
			}
			var posicion = GetCaretPos(campo);
			if(posicion == 2 || (posicion == 5 && tipo == 1) || (posicion == 11 && tipo == 3)){	
				if (posicion > valor.length || valor.charAt(posicion) != caracter) addText(campo,caracter);
			}
			return (true);
	}
}

function GetCaretPos(TxtObj) {
	var pos = 0;
	if (TxtObj.createTextRange) {
		rg = document.selection.createRange().duplicate();
		rg.moveStart('textedit',-1);
		pos = rg.text.length;
	} else if (TxtObj.setSelectionRange) {
		pos = TxtObj.selectionEnd;
	}
	return pos;
}

function addText( input, insText ) {
	input.focus();
 	if( input.createTextRange ) {
   		document.selection.createRange().text += insText;
 	} else if( input.setSelectionRange ) {
   		var len = input.selectionEnd;
   		input.value = input.value.substr( 0, len ) + insText + input.value.substr( len );
   		input.setSelectionRange(len+insText.length,len+insText.length);
 	} else { 
 		input.value += insText; 
 	}
}

function enableField (obj){
	if (document.getElementById(obj)){
		document.getElementById(obj).style.display = "block"; 
	}
}

function disableField (obj){
	if (document.getElementById(obj)){
		document.getElementById(obj).style.display = "none"; 
	}
}

