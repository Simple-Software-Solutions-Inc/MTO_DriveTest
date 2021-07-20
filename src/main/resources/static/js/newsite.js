
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

$("#cpwd1").click(function(event) {
	$("#requestPwd").toggleClass("d-none");
	// $("#changePwd").hide(1000);
	$("#loginRequest").toggleClass("d-none");
	event.preventDefault();
	console.log("This link was click");
  //$("#changePwd").hide(1000);
   $("#loginRequest").toggleClass("d-none");
   event.preventDefault();
  console.log("This link was click");
});

$("#requestbtn").click(function(event) {

	const
	url = "http://localhost:8080/reset-password";
	const
	data = {
		email : $("#request_email").val(),
	};

	$.post(url, data, function(data, status) {
		console.log(data);
		if (data === "data") {
			$("#requestPwd").toggleClass("d-none");
			$("#changePwd").toggleClass("d-none");
			console.log('This link was click');
		} else if (data === "no-data") {
			$("#resetErr").toggleClass("d-none");
			$("#resetErr").html("Sorry, this email address does not exist");
		}

	});

	event.preventDefault();

});

$("#changePwdbtn").click(
		function(event) {

			const
			url = "http://localhost:8080/change-password";
			const
			datap = {
				auth_code : $("#aCode").val(),
				new_password : $("#origPwd").val(),
			};

			$.post(url, datap, function(datap, status) {
				console.log(datap);
				if (datap === "data") {
					$("#changePwd").toggleClass("d-none");
					$("#loginRequest").toggleClass("d-none");

					$("#loginMsg").toggleClass("d-none");
					$("#loginMsg").html("Your password has been updated!");
					console.log("This link was click");
				} else if (datap === "no-data") {
					$("#resetChngErr").toggleClass("d-none");
					$("#resetChngErr").html(
							"Sorry, this authentication code is invalid");
				}

			});

			event.preventDefault();

		});

// Controls the disabling of the update password button
const
elemId = [ "#confPwd", "#aCode", "#origPwd" ];

elemId.forEach(function(item) {
	$(item).change(function() {

		var empty = true;
		elemId.forEach(function(item) {
			empty = empty && ($(item).val() !== '');
		});
		console.log("text chnage");

		// false to show
		if ($("#origPwd").val() === $("#confPwd").val() && empty) {
			$('#changePwdbtn').prop('disabled', false);
		} else {
			$('#changePwdbtn').prop('disabled', true);
		}
	});
});

//*******************************javascript code for qcassignment.html
// category assignment
var selected = [];
$('div#scroll input[type=checkbox]').change(function() {
	if (this.checked) {
		// Do stuff
		selected.push(parseInt($(this).attr('value')));
		if (selected.length > 0 ){
			if ($('#assignbtn').val() != "undefined") {
			$('#assignbtn').prop('disabled', false);
			}
			if ($('#unassignbtn').val() != "undefined") {
				$('#unassignbtn').prop('disabled', false);
				}
		}
	} else {
		const
		index = selected.indexOf(parseInt($(this).attr('value')));
		if (index > -1) {
			selected.splice(index, 1);
		}
		if (selected.length == 0 && $('#assignbtn').val() != "undefined") {
			$('#assignbtn').prop('disabled', true);
		}
		if (selected.length == 0 && $('#unassignbtn').val() != "undefined") {
			$('#unassignbtn').prop('disabled', true);
		}
	}

	console.log(selected);
	console.log($('#category_id').val());
	console.log($('#action_id').val());
});
//

if($('#action_id').val()!=='All' && $('#category_id').val()!=='All'){
	$('#titleCategory').html($('#action_id').val() + ' ' + $('#category_id').children()[$('#category_id').prop('selectedIndex')].text  + ' Questions');
}



$('#categoryReset').change(function() {
	if (this.checked){
		$('#category_id').prop('disabled', false);
		$('#action_id').prop('disabled', false);
		$('#categorySearch').prop('disabled', false);
		$(this).parent().toggleClass("d-none");
		if($('#assignbtn') !=undefined ){
			$('#assignbtn').parent().parent().parent().toggleClass("d-none");
		}
		if($('#unassignbtn') !=undefined){
			$('#unassignbtn').parent().parent().parent().toggleClass("d-none");
		}
		
		console.log("This link was click");
	}
	
});
const btnfunc = ['#assignbtn', '#unassignbtn'];
btnfunc.forEach(function(item) {
$(item).click(function(evt) {
	const
	url = "http://localhost:8080/dashboard/question-categories";
	const
	data_assignment = {
		questions : JSON.stringify(selected),
		category_id : parseInt($('#category_id').val()),
		action_id : $(item).val(),
	};
	var t =$(this);
	$.post(url, data_assignment, function(data_assignment, status) {
		console.log(data_assignment);
		if (data_assignment === "assigned") {
			//do something with response
			
			t.prop('disabled', true);
			console.log(selected);
			selected.forEach(function(item){
				$("#chk_" + item).parent().parent().parent().remove();
			});
			selected = [];
		} else if (data_assignment === "no-data") {
			selected = [];
			t.prop('disabled', true);
			console.log("This link was click");
		}

	});

	evt.preventDefault();
});
});
//*******************************end of javascript code for qcassignment.html