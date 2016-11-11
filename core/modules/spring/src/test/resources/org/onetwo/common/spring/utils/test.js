var validate = function(user){
	if(user.id===100){
		return true;
	}
	return false;
}
function validate2(user){
	if(user.id===100){
		return true;
	}
	return false;
}
function throwErrorFunc(context){
	if(context){
		context.error('抛出java错误！')
	}
	throw msg;
}

var funcObj = {}
funcObj.say = function(something){
	return 'say:'+something;
}

//for importClass
load("nashorn:mozilla_compat.js")
function newHashSet(){
	importClass(java.util.HashSet);
	var set = new HashSet();
	set.add('aa');
	set.add('bb');
	return set;
}
importPackage(java.util);
function newDate(){
	return new Date();
}
//importPackage(java.util);
function newJavaDate(){
	var JavaDate = Java.type("java.util.Date");
	return new JavaDate();
}
importClass(java.lang.System);
function isSameDay(){
	var JavaDate = Java.type("java.util.Date");
	var javadate = new JavaDate();
	//println class java.lang.Integer
	System.out.println(javadate.getDate().getClass())
	var jsdate = new Date();
	//println class java.lang.Double
	System.out.println(jsdate.getDate().getClass())
	return javadate.getDate()==jsdate.getDate();
}


