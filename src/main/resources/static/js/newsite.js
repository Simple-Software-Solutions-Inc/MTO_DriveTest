/**
 * 
 */

$("#cpwd1").click(function(event) {
	$("#requestPwd").toggleClass("d-none");
	// $("#changePwd").hide(1000);
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
	if($('#category_id').children()[$('#category_id').prop('selectedIndex')] != undefined){
		$('#titleCategory').html($('#action_id').val() + ' ' + $('#category_id').children()[$('#category_id').prop('selectedIndex')].text  + ' Questions');
	}
	
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



//Add the following code if you want the name of the file appear on select
$(".custom-file-input").change(function() {
  var fileName = $(this).val().split("\\").pop();
  //console.log(fileName);
  $(this).siblings(".custom-file-label").addClass("selected").html(fileName);
});

var orderDetail = [];
var orderSum = 0;
//const orderDetail = new Set()
$('#storeProducts button').click(function(){
	var product = {
			name: $(this).parent().prev().text(),
			value: parseFloat($(this).prev().text()),
	};
	console.log("" + $(this).parent().prev().text());
	console.log("" + $(this).prev().text());
	console.log(product);
	if(orderDetail.length>0){
		console.log("in loop");
		var exist = false;
		orderDetail.forEach(function(item){
			if (item.name === product.name && item.value === product.value){
				exist = true;
			}
		});
		if (!exist){
			orderDetail.push(product);
			orderSum += parseFloat(product.value);
			$('#itemCount').html('Number of Item: ' + orderDetail.length);
			$('#totalCost').html('Total Cost: $' + orderSum);
			
		}
		
	}else{
		orderDetail.push(product);
		orderSum += parseFloat(product.value);
		$('#itemCount').html('Number of Item: ' + orderDetail.length);
		$('#totalCost').html('Total Cost: $' + orderSum);
	}
	 
	console.log(orderDetail);
});

$('#gotoCart').click(function(evt){
	$(this).prev().val(JSON.stringify(orderDetail));
});


var billingBalance = 0;
console.log($('#paymentContainer').attr("data-total"));
var val = $('#paymentContainer').attr("data-total");

//if(paypal !== undefined){
//	paypal.Buttons({
//	    createOrder: function(data, actions) {
//	      // This function sets up the details of the transaction, including the amount and line item details.
//	      return actions.order.create({
//	        purchase_units: [{
//	          amount: {
//	        	currency_code: "USD",
//	            value: $('#paymentContainer').attr("data-total")
//	          }
//	        }]
//	      });
//	    },
//	    onApprove: function(data, actions) {
//	      // This function captures the funds from the transaction.
//	      return actions.order.capture().then(function(details) {
//	        // This function shows a transaction success message to your buyer.
////	        alert('Transaction completed by ' + details.payer.name.given_name);
////	        window.location.replace("http://localhost:8080/store/");
//	        $('#order').html('<div class="col  rounded  display-3"><i class="fa fa-check-circle" style="font-size:100px;color:green"></i>Payment Complete!</div>')
//	      });
//	    }
//	  }).render('#paypal-button-container');
//	  //This function displays Smart Payment Buttons on your web page.
//
//}


//registration
$('#breadcrumbsList').children().last().find('a').removeAttr("href");

//validation code for the registration form
$('#btn2').click(function(event){
	var filled = false;
	var checkfield = ["female", "male"];
	for(const elem of checkfield){
		filled = filled || $('#'+ elem).is(':checked');
	}


	
	var inputFields = ["first_name", "last_name", "nationality", "dob", "height", "address", "phone", "email", "password", "pwd2", "identification_type", "serial_no", "issue_date", "expiry_date", "issued_location", "file"];

	for(const elem of inputFields){
		filled = filled && ($('#'+ elem).val().trim()!=='');
	}

	filled = filled && ($("#password").val() == $("#pwd2").val()) && $('#location_id').find(":selected").text()!=='';
	
	if (!filled){
		alert('Please check the information you have entered.');
		getLabel('#registration', '.col-6');
		event.preventDefault();
	}
});


//const inputList = $('#registration input');
//var inputListId = [];
//for(const elem of inputList){
//	inputListId.push(elem.id);
//}
//console.log(inputListId);


//var getId = (idName, inputType)=>{
//	const inputList = $(idName + ' ' + inputType);
//	var inputListId = [];
//	for(const elem of inputList){
//		inputListId.push(elem.id);
//	}
//	return inputListId;
//}

//var selectArray = getId('#registration', 'select');
//var textArray = getId('#registration', 'input[type=text]');
//var radioArray = getId('#registration', 'input[type=radio]');
//var fileArray = getId('#registration', 'input[type=file]');
//var dateArray = getId('#registration', 'input[type=date]');
//var numberArray = getId('#registration', 'input[type=number]');
//var passwordArray = getId('#registration', 'input[type=password]');

var getLabel = (idName, inputType)=>{
	const inputList = $(idName + ' ' + inputType);
	var inputListId = [];
	for(const elem of inputList){
		 var txt3 = document.createElement("span");
		 txt3.append("*This field is required.")
		 txt3.className = 'text-danger';
		elem.append(txt3);
		inputListId.push(elem);
		
	}
	return inputListId;
}



//const inputField2 = textArray.concat(fileArray, numberArray, passwordArray, numberArray, fileArray, radioArray)
//for(const elem of inputField2){
//	filled = filled && ($('#'+ elem).val().trim()!=='');
//}

var defaultRecaptcha = {
		'transform': $('.g-recaptcha').css('transform'),
			'-webkit-transform': $('.g-recaptcha').css('-webkit-transform'),
			'transform-origin' : $('.g-recaptcha').css('transform-origin'),
			'-webkit-transform-origin': $('.g-recaptcha').css('-webkit-transform-origin')
};
console.log(defaultRecaptcha['-webkit-transform'] );

var width = $('.g-recaptcha').parent().width();
if (width < 302) {
	var scale = width / 302;
	$('.g-recaptcha').css('transform', 'scale(' + scale + ')');
	$('.g-recaptcha').css('-webkit-transform', 'scale(' + scale + ')');
	$('.g-recaptcha').css('transform-origin', '0 0');
	$('.g-recaptcha').css('-webkit-transform-origin', '0 0');
}

$( window ).resize(function() {
	var width = $('.g-recaptcha').parent().width();
	if (width < 302) {
		var scale = width / 302;
		$('.g-recaptcha').css('transform', 'scale(' + scale + ')');
		$('.g-recaptcha').css('-webkit-transform', 'scale(' + scale + ')');
		$('.g-recaptcha').css('transform-origin', '0 0');
		$('.g-recaptcha').css('-webkit-transform-origin', '0 0');
	}
	else{
		$('.g-recaptcha').css('transform', 'scale(1)');
		$('.g-recaptcha').css('-webkit-transform', 'scale(1)');
		$('.g-recaptcha').css('transform-origin', '0 0');
		$('.g-recaptcha').css('-webkit-transform-origin', '0 0');
		
	}
	});



//Control the quiz session so that the user dkoesn't leave
//$('#quizStart').click(function(event){
//	// Store
//	sessionStorage.setItem("quiz_start", true);
//	// Retrieve
////	document.getElementById("result").innerHTML = sessionStorage.getItem("lastname");
//	console.log(sessionStorage.getItem("quiz_start"));
////	event.preventDefault();
//});

if (window.location.href.includes('quiz/')){
	
	
	$('a').click(function(event){
		if ($(this).attr('href').includes( '/log-out')){
			var aHref = $(this).attr('href');
			var logout = $(this);
			if (confirm("If you log-out your quiz session will end. Are you sure you are ready to submit your answers?")){
				var radioVal = $('#options input[name=answer]:checked').val()=== undefined ? null : $('#options input[name=answer]:checked').val();

				
				const url = window.location.href;
				const data = {
					answer : radioVal,
					next: 'SUBMIT QUIZ'
				};
				
				logout.attr("data-toggle","modal");
				logout.attr("data-target", "#exampleModalCenter"); 
				
				$.post(url, data, function(data, status) {
					window.location.href =  window.location.origin + aHref
				});
					event.preventDefault();

			}
			else{
				event.preventDefault();
			}	
		}
		else if($(this).attr('data-lightbox') === undefined){
			$(this).attr("data-toggle","modal");
			$(this).attr("data-target", "#exampleModalCenterTest"); 
			event.preventDefault();
		}	
	});
//	console.log(window.location.href);
}


//Allow only one attempt once user has pass
$('#result_table td img').each(function(){
	if ($(this).attr('alt') === 'pass'){
//		$('#quizStart').toggleClass('btn-success');
//		$('#quizStart').toggleClass('btn-danger');
		
		$('#quizStart').click(function(evt){
			if ($('#congratsMsg').length===0){
				$("<span class='rounded bg-warning text-white p-2 mr-3' id='congratsMsg'><h5>Congratulations! You've already passed.</h5></span>").insertBefore('#quizStart').hide().fadeIn('slow', function(){
					$('#congratsMsg').delay(3000).fadeOut('slow', function(){
						$('#congratsMsg').remove();
					});
				});
				
			}
			evt.preventDefault();
		});
	}
});

//Hide the tour button
if (!window.location.href.includes('/main')){
	$('#tourBtn').toggleClass('d-none');
}

//
//console.log(width);

//For login form recapture
function validateForm(){
	if(grecaptcha.getResponse())
		{
			return true;
		}
	else
		{			 
		 if ($('#robotMsg').length===0){
			$("<span class='rounded bg-warning text-white p-1 mx-3 mb-3' id='robotMsg'><h5>Please prove that you're not robot.</h5></span>").insertBefore($('#submitBtn_id').prev()).hide().fadeIn('slow', function(){
				$('#robotMsg').delay(3000).fadeOut('slow', function(){
					$('#robotMsg').remove();
				});
			});
			
		}
		 return false;
		}
}