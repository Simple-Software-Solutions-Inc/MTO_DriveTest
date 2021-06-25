<<<<<<< HEAD
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
=======
/**
 * 
 */

$("#cpwd1").click(function(event){
  $("#requestPwd").toggleClass("d-none");
>>>>>>> 1b6e87209c6e13b9f93075e69e51450deec45759
  //$("#changePwd").hide(1000);
   $("#loginRequest").toggleClass("d-none");
   event.preventDefault();
  console.log("This link was click");
});


$("#requestbtn").click(function(event){

const url = "http://localhost:8080/reset-password";
const data = {
			email: $("#request_email").val(),
			};

$.post(url, data, function(data, status){
console.log(data);
});


  $("#requestPwd").toggleClass("d-none");
  $("#changePwd").toggleClass("d-none");
   event.preventDefault();
  console.log("This link was click");
});