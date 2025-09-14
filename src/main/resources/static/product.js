let billingType = 'DEL';

$(document).ready(function() {

	$("#uploadcontainer").hide();
	$("#searchid").on("keyup", function() {
		console.log($("#searchid").length);
		var value = $(this).val().toUpperCase();
		console.log("VALUE:", $(this).val().toUpperCase());
		if (value === "") {

			$.ajax({
				method: "GET",
				url: "/getAll?billingType="+billingType.toUpperCase(),
				success: function(data) {
					if (data.product && data.product.length > 0) {
						let serialNo = 1;
						$.each(data.product, function(index, product) {
							

							let statusRaw = (product.prodDispFlag || '').trim().toUpperCase();
							let stockStatus = statusRaw === "Y" ? "In Stock" : "Out of Stock";

							let tr = $('<tr></tr>');
							tr.append(`<td>${serialNo}</td>`);
							tr.append(`<td>${product.prod_name}</td>`);
							tr.append(`<td>${product.category || '-'}</td>`);
							tr.append(`<td>${product.price}</td>`);
							tr.append(`<td>${stockStatus}</td>`);
							tr.append(`<td><button class="icon-btn edit-btn" onclick="editProduct('${product.id}')">Edit</button></td>`);
							tr.append(`<td><button class="icon-btn delete-btn" onclick="deleteProduct('${product.id}')">Delete</button></td>`);
							$("#tableBody").append(tr);
							serialNo++;
						});
					}
				}
			});
		}
		else {
			$.ajax({
				method: "GET",
				url: "/get/productByName/" + value  + "?billingType="+billingType.toUpperCase(),
				success: function(data) {
					$("#tableBody").empty();
					$("#empty").hide();
					if (data.product && data.product.length > 0) {
						let serialNo = 1;
						$.each(data.product, function(index, product) {

							let statusRaw = (product.prodDispFlag || '').trim().toUpperCase();
							let stockStatus = statusRaw === "Y" ? "In Stock" : "Out of Stock";

							let tr = $('<tr></tr>');
							tr.append(`<td>${serialNo}</td>`);
							tr.append(`<td>${product.prod_name}</td>`);
							tr.append(`<td>${product.category || '-'}</td>`);
							tr.append(`<td>${product.price}</td>`);
							tr.append(`<td>${stockStatus}</td>`);
							tr.append(`<td><button class="icon-btn edit-btn" onclick="editProduct('${product.id}')">Edit</button></td>`);
							tr.append(`<td><button class="icon-btn delete-btn" onclick="deleteProduct('${product.id}')">Delete</button></td>`);
							$("#tableBody").append(tr);
							serialNo++;
						});
					}
					else {

						$("#empty").show();

					}
				}
			});

		}

	});

});




function editProduct(id) {
	
	$("#uploadcontainer").hide();
	$('#editContainer').addClass('active');
	$('#ProdDiv').addClass('blur');
	$("#EditTableDiv").show();
	$(".upload-section").hide();
	$("#savebtn").show();
	$('#heading').text('')
	$('#heading').append(
		  $('<div></div>').css({
		    width: '100%',
		    height: '50px',
		    display: 'flex',
		    alignItems: 'center',
		    justifyContent: 'left',
			marginLeft: '38px'
		   // backgroundColor: '#f0f0f0' // optional for better visibility
		  }).append(
		    $('<h1></h1>').text('EDIT PRODUCTS').css({
		      fontSize: '30px',
		      margin: 0
		    })
		  )
		);
	$.ajax({
		method: "GET",
		url: "/get/productById/" + id,
		success: function(data) {
			$("#editContainer").show();
			$("#prodname").val(data.product.prod_name);
			$("#prodcat").val(data.category.name).prop("disabled", true).addClass("disabled-cursor");;
			$("#prodsubcat").val(data.product.prodParentName).prop("disabled", true).addClass("disabled-cursor");;
			$("#prodprice").val(data.product.price);
			var status = (data.product.prodDispFlag || '').trim().toUpperCase();
			$("#prodstatus").val(status);

			/*<h1>Edit</h1>
			heading*/
			var input = $('<input>', {
				type: 'hidden',
				id: 'categoryId',
				class: 'cat-input',
				value: data.category.id
			});
			var input1 = $('<input>', {
				type: 'hidden',
				id: 'productId',
				class: 'cat-input',
				value: data.product.id
			});
			var div = $('<div></div>');
			div.append(input);
			div.append(input1);
			$("#editContainer").append(div);

		},
		error: function(xhr) {
			console.error("Failed to fetch product:", xhr);
		}
	});


}
$(document).on('keydown', 'input', function(e) {
    if (e.key === 'Enter') {
        e.preventDefault(); // Stops Enter from triggering form submission
        return false;
    }
});

$(document).on("click", "#savebtn", function(e) {

	e.preventDefault();	
	var flag;
	
	if (!$("#prodsubcat").is(":disabled") && !$("#prodcat").is(":disabled")) {
	let checkData = {

		prod_name: $("#prodname").val().trim(),
		subcategory: $("#prodsubcat").val().trim(),
		category: $("#prodcat").val().trim()

	};
	$.ajax({
		method: "POST",
		url: "/check/category",
		contentType: "application/json",
		data: JSON.stringify(checkData),
		async: false,
		success: function(data) {

			if (!(data)) {
				Swal.fire({
					title: 'Poduct is Present',
					text: 'Product Already Present With Same Category!',
					icon: "warning",
					timer: 1500,
					showConfirmButton: false
				});
				flag = false;
				return false;
			} else {
				flag = true;
			}
		},
		complete: function() {
			//location.reload();
		},
		error: function(xhr) {
			console.error("Error saving product:", xhr);
		}
	});
}
	
if ($("#prodcat").is(":disabled")) {

			id = $("#productId").val();
			cid = $("#categoryId").val();
		}
		let prodParentName = $("#prodsubcat")?.val() || "NA";



		let payload = {
			id: id,
			prod_name: $("#prodname").val(),
			prodParentName: prodParentName,
			price: $("#prodprice").val(),
			prodDispFlag: $("#prodstatus").val(),

		};

		let category = {
			id: cid,
			name: $("#prodcat").val(),

		};

		let fullData = {
			product: payload,
			category: category
		};
	
	
		 if(!flag && flag != false){

			$.ajax({
				method: "POST",
				url: "/update/products?billingType="+billingType.toUpperCase(),
				contentType: "application/json",
				data: JSON.stringify(fullData),
				success: function(data) {
					Swal.fire({
					    title: 'Success!',
					    text: 'Product Edited Successfully!',
					    icon: 'success',
					    timer: 1500,
					    showConfirmButton: false
					});

					console.log("Product saved:", data);
				},
				complete: function() {
					setTimeout(function () {
					    location.reload();
					}, 1500);
				},
				error: function(xhr) {
					console.error("Error saving product:", xhr);
				}
			});

		}
	
	if (flag) {
		var cid = null;
		var id = null;
		

		if (id == null && cid == null) {

			$.ajax({
				method: "POST",
				url: "/save/products?billingType="+billingType.toUpperCase(),
				contentType: "application/json",
				data: JSON.stringify(fullData),
				success: function(data) {
					Swal.fire({
					    title: 'Success!',
					    text: 'Product Added Successfully!',
					    icon: 'success',
					    timer: 2500,
					    showConfirmButton: false
					});

					console.log("Product saved:", data);
				},
				complete: function() {

					setTimeout(function () {
					    location.reload();
					}, 1500);
					
				},
				error: function(xhr) {
					console.error("Error saving product:", xhr);
				}
			});

		} 
	}

});

$(document).on("click", "#add", function() {
	$('#heading').text('')
	$('#heading').append(
		  $('<div></div>').css({
		    width: '100%',
		    height: '50px',
		    display: 'flex',
		    alignItems: 'center',
		    justifyContent: 'left',
			marginLeft: '38px'
		   // backgroundColor: '#f0f0f0' // optional for better visibility
		  }).append(
		    $('<h1></h1>').text('ADD PRODUCTS').css({
		      fontSize: '30px',
		      margin: 0
		    })
		  )
		);
	$('#editContainer').addClass('active');
	$("#editContainer").show();
	$('#ProdDiv').addClass('blur');
	$("#EditTableDiv").show();
	$(".upload-section").hide();
	$("#savebtn").show();
	$("#prodname").val("");
	$("#prodcat").val("").prop("disabled", false);
	$("#prodsubcat").val("").prop("disabled", false);
	$("#prodprice").val("");
	$("#prodstatus").val("");

	$(document).on("blur", "#prodsubcat", function() {




	});

});



function deleteProduct(id) {

	if (confirm("Are you sure you want to delete this item? This action cannot be undone.")) {
		$.ajax({
			method: "DELETE",
			url: "/delete/productById/" + id,
			success: function(data) {
				Swal.fire({
					    title: 'Success!',
					    text: 'Product Deleted Successfully!',
					    icon: 'success',
					    timer: 1500,
					    showConfirmButton: false
					});
			},

			complete: function() {
				setTimeout(function () {
				    location.reload();
				}, 1500);
			},
			error: function(xhr) {
				console.error("Failed to fetch product:", xhr);
			}
		});
	}

}

/*$('#uploadBtn').on('click', function() {

	let fileName = $(this).closest('.upload-section').find('input[type="file"]');

	var fileInput = $('#excelFile')[0];
	if (!fileInput.files.length) {
		$('#response').text('Please select a file.');
		return;
	}

	var file = fileInput.files[0];
	var formData = new FormData();
	formData.append('file', file); // 'file' must match Spring controller param

	$.ajax({
		url: '/upload',  // Replace with your actual backend endpoint URL
		type: 'POST',
		data: formData,
		contentType: false,  // Important to let jQuery set multipart boundary
		processData: false,  // Important to send FormData as is
		success: function(res) {
			$('#response').text(res.result);
		},
		error: function(xhr) {
			$('#response').text('Upload failed: ' + xhr.responseText);
		}
	});
});
*/

$('.uploadBtn').click(function() {
	const fileInput = $(this).closest('.upload-wrapper').find('input[type="file"]');
	const file = fileInput[0].files[0];
	const inputId = fileInput.attr('id');

	if (!file) {
		Swal.fire({
			title: 'Upload File!',
			text: 'Please Select a File First!',
			icon: "warning",
			timer: 1500,
			showConfirmButton: false
		});
		return;
	}

	const allowedTypes = [
		'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet',
		'application/vnd.ms-excel'
	];
	if (!allowedTypes.includes(file.type)) {
		Swal.fire({
			title: 'Invalid File!',
			text: 'Only Excel files (.xls, .xlsx) are allowed.',
			icon: "error",
			timer: 1500,
			showConfirmButton: false
		});
		return;
	}

	if (inputId === "ProdExcelFile") {
		console.log("Uploading to product Excel endpoint");
		uploadFile(file, "/upload/purchase");
	} else {
		alert("Unrecognized input field ID: " + inputId);
	}
});
// Common function to upload file via AJAX
function uploadFile(file, endpoint) {
	var formData = new FormData();
	formData.append("file", file);

	$.ajax({
		url: endpoint,
		type: "POST",
		data: formData,
		contentType: false,
		processData: false,
		success: function(res) {
			let html = '';
			Swal.fire({
			    title: 'Success!',
			    text: 'Purchase Added Successfully!',
			    icon: 'success',
			    timer: 2500,
			    showConfirmButton: false
			});
			if (res.success) {
				html += '<div class="alert alert-success">Upload successful. Rows processed: ' + res.processedRows + '</div>';
			} else {
				if (Array.isArray(res.messages)) {
					res.messages.forEach(msg => {
						alert(msg);  // show alert for each validation message
					});
				} else {
					alert(res.messages);
				}
			}

			$('#response').append(html);

		},
		error: function(xhr) {
			try {
				const errors = JSON.parse(xhr.responseText);
				if (Array.isArray(errors)) {
					let html = '<div class="alert alert-danger"><ul>';
					errors.forEach(error => {
						html += '<li>' + error + '</li>';
					});
					html += '</ul></div>';
					$('#response').html(html);
				} else {
					$('.#response').html('<div class="alert alert-danger">' + xhr.responseText + '</div>');
				}
			} catch (e) {
				$('#response').html('<div class="alert alert-danger">Upload failed: ' + xhr.responseText + '</div>');
			}
		}
	});
}



function closeEdit() {
	$('.sheet-popup').hide();
	$('#editContainer').removeClass('active');
	$('#ProdDiv').removeClass('blur');
	$("#uploadcontainer").hide();
}

$(document).on("click", "#Purchases", function() {
	$(".upload-wrapper").show();
	$('#heading').text('')
	$('#heading').append(
	  $('<div></div>').css({
	    width: '100%',
	    height: '50px',
	    display: 'flex',
	    alignItems: 'center',
	    justifyContent: 'left',
		marginLeft: '38px'
	   // backgroundColor: '#f0f0f0' // optional for better visibility
	  }).append(
	    $('<h1></h1>').text('PURCHASE UPLOAD').css({
	      fontSize: '30px',
	      margin: 0
	    })
	  )
	);
	$('#editContainer').addClass('active');
	$("#editContainer").show();
	$('#ProdDiv').addClass('blur');
	$("#EditTableDiv").hide();
	$(".upload-section").show();
	$("#savebtn").hide();

});


function downloadExcel() {
	const link = document.createElement('a');
	link.href = '/files/Purchase Template.xlsx';
	link.download = 'Purchase Template.xlsx';
	document.body.appendChild(link);
	link.click();
	document.body.removeChild(link);
}


$('input[type="text"]').on('keyup', function(e) {

		$(this).val($(this).val().toUpperCase());
});

$(document).on('click','.ProdType', function(){
	$('#searchid').val('');
	billingType = $(this).data('type');
	
	$.ajax({
			method: "GET",
			url: "/getAll?billingType="+billingType.toUpperCase(),
			success: function(data) {
				$("#tableBody").empty();
				$("#empty").hide();
				if (data.product && data.product.length > 0) {
					let serialNo = 1;
					$.each(data.product, function(index, product) {

						let statusRaw = (product.prodDispFlag || '').trim().toUpperCase();
						let stockStatus = statusRaw === "Y" ? "In Stock" : "Out of Stock";

						let tr = $('<tr></tr>');
						tr.append(`<td>${serialNo}</td>`);
						tr.append(`<td>${product.prod_name}</td>`);
						tr.append(`<td>${product.category || '-'}</td>`);
						tr.append(`<td>${product.price}</td>`);
						tr.append(`<td>${stockStatus}</td>`);
						tr.append(`<td><button class="icon-btn edit-btn" onclick="editProduct('${product.id}')">Edit</button></td>`);
						tr.append(`<td><button class="icon-btn delete-btn" onclick="deleteProduct('${product.id}')">Delete</button></td>`);
						$("#tableBody").append(tr);
						serialNo++;
					});
				}
				else {

					$("#empty").show();

				}
			}
		});
	
});

function setActive(button) {
  document.querySelectorAll('.d-btn').forEach(btn => btn.classList.remove('active'));
  button.classList.add('active');
}
