$(document).ready(function() {
console.log("hello there");
var enjoyhint_instance = new EnjoyHint({});
var enjoyhint_script_steps = [
  {
    'click #bttn' : 'Click the New button to start creating your project'
  }  
]; 
//set script config
enjoyhint_instance.set(enjoyhint_script_steps);
//run Enjoyhint script
enjoyhint_instance.run();
 
});
$("#cpwd1").click(function(){
  $("#requestPwd").hide(1000);
  //$("#changePwd").hide(1000);
   $("#loginRequest").hide(1000);
  
});