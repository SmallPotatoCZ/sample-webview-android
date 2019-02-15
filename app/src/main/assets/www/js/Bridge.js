var Bridge={};

Bridge.login=function(state){
var message={method:"login",state:state};
prompt(BRIDGE_KEY,JSON.stringify(message));
}

Bridge.callBack = function(result){

     if(result.success){

          alert("login success");

     }

}