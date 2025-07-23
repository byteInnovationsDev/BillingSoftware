



$(document).on("click", "#genreport", function() {


	var fromdate = $("#FromDate").val();
	var Todate = $("#ToDate").val();

	var reportType = $("#reportType").val();
	$('#saveLoader').show();
	$('.layout').addClass("blur");
	if (!reportType) {
		Swal.fire({
			title: 'Report Type is Empty!',
			text: 'Please Select The Report Type',
			icon: "warning",
			timer: 1500,
			showConfirmButton: false
		});
		$('#saveLoader').hide();
		$('.layout').removeClass("blur");
		return;

	}

	else if (!fromdate) {
		Swal.fire({
			title: 'From Date is Empty!',
			text: 'Please Select The From Date',
			icon: "warning",
			timer: 1500,
			showConfirmButton: false
		});
		$('#saveLoader').hide();
		$('.layout').removeClass("blur");
		return;

	}
	else if (!Todate) {
		const today = new Date();
		const year = today.getFullYear();
		const month = String(today.getMonth() + 1).padStart(2, '0');
		const day = String(today.getDate()).padStart(2, '0');
		Todate = `${year}-${month}-${day}`;

	}

	const url = `/genReports?reporttype=${encodeURIComponent(reportType)}&fromdate=${encodeURIComponent(fromdate)}&todate=${encodeURIComponent(Todate)}`;

	fetch(url)
		.then(response => response.blob())
		.then(blob => {
			const url = window.URL.createObjectURL(blob);
			const a = document.createElement('a');
			$('#saveLoader').hide();
			$('.layout').removeClass("blur");
			a.href = url;
			a.download = "report.xlsx";
			document.body.appendChild(a);
			a.click();
			a.remove();
		})
		.catch(() => alert("Something went wrong."));
});


