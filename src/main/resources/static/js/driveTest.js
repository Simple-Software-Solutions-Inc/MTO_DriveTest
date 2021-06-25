$("#target").click(function(evt) {
	// get size of report page
	var reportPageHeight = $("#charts").innerHeight();
	var reportPageWidth = $("#charts").innerWidth();

	// create a new canvas object that we will populate with all other canvas objects
	var pdfCanvas = $("<canvas />").attr({
		id: "canvaspdf",
		width: reportPageWidth,
		height: reportPageHeight * 2,
	});

	// keep track canvas position
	var pdfctx = $(pdfCanvas)[0].getContext("2d");
	var pdfctxX = 0;
	var pdfctxY = 0;
	var buffer = 100;

	// for each chart.js chart
	$("canvas").each(function(index) {
		// get the chart height/width
		var canvasHeight = $(this).innerHeight();
		var canvasWidth = $(this).innerWidth();

		// draw the chart into the new canvas
		pdfctx.drawImage($(this)[0], pdfctxX, pdfctxY, canvasWidth, canvasHeight);
		pdfctxY += canvasHeight + buffer;
		// our report page is in a grid pattern so replicate that in the new canvas
	});

	var doc = new jsPDF({format: "letter"});
	var elementHTML = $("#charts").html();
	var specialElementHandlers = {
		"#elementH": function(element, renderer) {
			return true;
		},
	};

	doc.addImage($(pdfCanvas)[0], "PNG", 15, 15);

	doc.addPage("letter", "portrait");
	var y = 20;
	doc.setLineWidth(2);
	doc.text(15, 15, "Applicant That Pass The Drive Test Quiz");
	doc.autoTable({
		tableLineColor: [255, 255, 255],
		tableLineWidth: 0.05,
		html: "#simple_table",
		startY: 20,
		bodyStyles: { lineColor: [0, 0, 0] },
		theme: "striped",
	});

	// Save the PDF
	doc.save("sample-document.pdf");
	evt.preventDefault();
}
);

function worker() {
	$.ajax({
		url: "http://localhost:8080/results/graduates",
	}).then(function(data) {
		var html;
		$.each(data, function(key, value) {
			html +=
				"<tr>" +
				"<td>" +
				value.first_name +
				" " +
				value.last_name +
				"</td>" +
				"<td>" +
				value.dob +
				"</td>" +
				"<td>" +
				value.address +
				", " +
				value.location_id.zip_code +
				", " +
				value.location_id.city +
				", " +
				value.location_id.province +
				"</td>" +
				"</tr>";
		});
		var tabledata = $("#tdata").html(html);
		setTimeout(worker, 30000);
	});
	console.log("Test this");
}
worker();



// === include 'setup' then 'config' above ===

const labels = [
	'January',
	'February',
	'March',
	'April',
	'May',
	'June',
];
const data = {
	labels: labels,
	datasets: [{
		label: 'DriveTest Quiz Attempt Projection',
		backgroundColor: 'rgb(255, 99, 132)',
		borderColor: 'rgb(255, 99, 132)',
		data: [100, 110, 125, 92, 80, 130, 95],
	}]
};


const config = {
	type: 'line',
	data,
	options: {}
};

var myChart = new Chart(
	document.getElementById('myChart'),
	config
);



function worker1() {
	$.ajax({
		url: "http://localhost:8080/results/graduates/passrate",
	}).then(function(data) {
		const DATA_COUNT = 5;
		const NUMBER_CFG = { count: DATA_COUNT, min: 0, max: 100 };

		const data1 = {
			labels: ["Fails", "Passes"],
			datasets: [
				{
					label: "Dataset 1",
					data: data,
					backgroundColor: ["rgb(255, 99, 132)", "rgb(164, 247, 158)"],
				},
			],
		};

		const config1 = {
			type: "pie",
			data: data1,
			options: {
				responsive: true,
				plugins: {
					legend: {
						position: "bottom",
					},
					title: {
						display: true,
						text: "G1 DriveTest Quiz Success Rate",
					},
				},
			},
		};

		var myChart1 = new Chart(document.getElementById("myChart1"), config1);

	});
	console.log("Test3 this");
}

setInterval(worker1(), 3000);
