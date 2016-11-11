
load("nashorn:mozilla_compat.js")
importClass(java.lang.System);

var hello = 'world';
System.out.println('before return')
if(true){
	return;
}
System.out.println('after return')
function funcAfterReturn(){
	
}