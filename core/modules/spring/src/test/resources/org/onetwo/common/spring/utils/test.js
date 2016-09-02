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