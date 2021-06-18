/**
 * 
 */

$("#cpwd1").click(function(event){
  $("#requestPwd").toggleClass("d-none");
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