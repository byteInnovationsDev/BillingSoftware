// Main product/category/subcategory handlers
let category;
let subCategory;
let isBillSaved = false;
let billingType;
let isAdmin = null;

$(document).on("click", "#list li", function() {
	category = $(this).text();
	const items = document.querySelectorAll('.menu-list li');
	items.forEach(item => item.classList.remove('active'));
	$(this).addClass('active');
	$('#prodlist').empty();
	$("#prodData").empty();
	$.ajax({
		method: "GET",
		url: "/get/product/" + category + "?billingType="+billingType,
		success: function(data) {
			
			$('#prodlist').empty();

			   if (!data || data.length === 0) {
			       const emptyMsg = $('<div></div>')
			           .text('NO PRODUCTS FOUND FOR THE CATEGORY '+category.toUpperCase())
			           .css({
			               textAlign: 'center',
			               color: 'red',
			               fontWeight: 'bold',
			               marginTop: '100px',
						   marginLeft : '20%',
			               fontSize: '16px'
			           });
			       $('#prodlist').append(emptyMsg);
			       return;
			   }
			
			if (data[0].prodParentFlag === 'Y') {
				$('#list').empty();
				$("#backbtn").show();
			}
			
			$.each(data, function(_, val) {
				if (val.prodParentFlag === 'Y') {
					$('#list').css({
					    display: 'none'
					});
					$("#sublist").show()
					$("#sublist").append($('<li>').text(val.prodParentName));
				} else {
					//const li = $('<li>').text(val.prod_name).css('list-style-type', 'none');
					
					try {
					        console.log("Rendering:", val.prod_name);

					        const span = $('<span>')
					            .text(val.prod_name)
					            .css({
					                position: 'absolute',
					                top: '10%',
					                left: '10%',
					                right: '10%'
					            });

					        const div = $('<div></div>')
					            .addClass('products quantity-wrapper ')
					            .css({ position: 'relative' })
					            .append(span);

					        if (val.prodDispFlag === "N") {
					            div.css({
					                "pointer-events": "none",
					                "opacity": "0.6"
					            });

					            const outOfStockLabel = $('<div class="outOfStock"></div>')
					                .text('Out of Stock')
					                .css({
					                    position: 'absolute',
					                    top: '50%',
					                    left: '45%',
					                    transform: 'translate(-50%, -50%)',
					                    color: 'red',
					                    fontWeight: 'bold',
					                    fontSize: '16px',
					                    textAlign: 'left'
					                });

					            div.append(outOfStockLabel);
					        }

					        $("#prodlist").append(div);
					        $(".quantity-wrapper ").css("position", "relative");

					        const input = $('<input>', {
					            type: 'text',
					            id: val.prod_name,
					            class: 'product-input',
					            price: val.price,
					            category: category,
					            subcategory: val.prodSubCategory || "NA"
					        });

					        $("#prodData").append(input);

					    } catch (e) {
					        console.error("Error rendering product:", val, e);
					}    }
			});
		}
	});
});

$(document).on("click", "#sublist li", function() {
	subCategory = $(this).text();
	const items = document.querySelectorAll('.menu-list li');
		items.forEach(item => item.classList.remove('active'));
		$(this).addClass('active');
	$("#backbtn").show();
	$('#prodlist').empty();
	$("#prodData").empty();
	$.ajax({
		method: "GET",
		url: "/get/productsByName/" + subCategory + "?billingType="+billingType,
		success: function(data) {
			$.each(data, function(_, val) {
				
					//const li = $('<li>').text(val.prod_name).css('list-style-type', 'none');
					const span = $('<span>')
					    .attr('id', 'prodName') 
					    .text(val.prod_name)
					    .css({
					        position: 'absolute',
					        top: '10%',
					        left: '10%',
					        right: '10%'
					    });
					const div = $('<div></div>')
					    .addClass('products quantity-wrapper')
					    .css({
					        position: 'relative' // Important for absolute children
					    })
					    .append(span);

					if (val.prodDispFlag === "N") {
					    div.css({
					        "pointer-events": "none",
					        "opacity": "0.6"
					    });

					    const outOfStockLabel = $('<div></div>').text('Out of Stock').addClass('outOfStock');
							
							div.append(outOfStockLabel);
						}
					
					$("#prodlist").append(div);
					$(".quantity-wrapper ").css("position","relative")

					const input = $('<input>', {
						type: 'text',
						id: val.prod_name,
						class: 'product-input',
						price: val.price,
						category: category,
						subcategory: subCategory
					});
					$("#prodData").append(input);
				
				
			});
		}
	});
});

$(document).on("click", "#backbtn", function() {
	$('#list, #sublist, #prodlist').empty();
	$("#backbtn").hide();
	$.ajax({
		method: "GET",
		url: "/get/Categories",
		success: function(data) {
			$.each(data, function(_, val) {
				$("#list").append($('<li>').text(val.name));
			});
			$("#list").show();
		}
	});
});

$(document).on("click", ".products", function () {
	const $productDiv = $(this);
	$productDiv.css('background', 'linear-gradient(135deg, #e0f0ff, #fde4f7)');
	// Only add quantity-container once
	if ($productDiv.find('.quantity-container').length === 0) {
		const productName = $productDiv.text().trim();
		const price = parseFloat($(`#${CSS.escape(productName)}`).attr("price")) || 0;
		const safeId = sanitizeId(productName);
		const unitPrice = price;
		const qtyId = `${safeId}-${unitPrice}-qty`;
		const existingQty = $(`#${qtyId}`).text();

		const qtyContainer = $('<div>').addClass('quantity-container ').append(
			
			
			$('<span class="qty-btn minus"> - </span>'),
			$('<span class="num quantity-display">0</span>'),
			$('<span class="qty-btn add"> + </span>'),
		);

		// If previous quantity exists, set it
		if (existingQty && !isNaN(existingQty)) {
			qtyContainer.find('.num').text(existingQty);
		}

		// Append qty container after existing content
		$productDiv.append(qtyContainer);
	}
});


function sanitizeId(str) {
	return str.trim().toLowerCase().replace(/\s+/g, '-').replace(/[^\w\-]/g, '');
}

$(document).ready(function () {
	
	$(document).off("click", ".add").on("click", ".add", handleClick);
	$(document).off("click", ".minus").on("click", ".minus", handleClick);

	function handleClick() {
		const productDiv = $(this).closest(".products");
		const numSpan = productDiv.find(".num");
		let currentQty = parseInt(numSpan.text(), 10) || 0;
		const isAdd = $(this).hasClass("add");
		const newQty = isAdd ? currentQty + 1 : Math.max(0, currentQty - 1);
		numSpan.text(newQty);

		// ✅ Get product name from the first <span> (NOT by ID or class)
		const productName = productDiv.find("span").first().text().trim();

		// ✅ Safely find the matching input
		const productInput = $(`#prodData input[id='${productName}']`);
		const unitPrice = parseFloat(productInput.attr("price")) || 0;
		const cat = productInput.attr("category") || "General";
		const subCat = productInput.attr("subcategory") || "NA";

		// ✅ Create safe ID key
		const safeId = sanitizeId(productName);
		const uniqueKey = `${safeId}-${unitPrice}`;
		const rowId = `row-${uniqueKey}`;
		const qtyId = `${uniqueKey}-qty`;
		const priceId = `${uniqueKey}-price`;
		const catId = `${uniqueKey}-category`;
		const subCatId = `${uniqueKey}-subcat`;
		const totalPrice = (unitPrice * newQty).toFixed(2);
		const existingRow = $("#" + rowId);

		if (newQty === 0) {
			existingRow.remove();
		} else {
			if (existingRow.length) {
				existingRow.find(`#${qtyId}`).text(newQty);
				existingRow.find(`#${priceId}`).text(totalPrice);
			} else {
				const row = $('<tr>', { id: rowId, style: "display:none;" }).append(
					$('<td>').append($('<span>', { id: `${uniqueKey}-product` }).text(productName)),
					$('<td>').append($('<span>', { id: qtyId }).text(newQty)),
					$('<td>').append($('<span>', { id: priceId }).text(totalPrice)),
					$('<td>').append($('<span>', { id: catId }).text(cat)),
					$('<td>').append($('<span>', { id: subCatId }).text(subCat))
				);
				$("#TableDiv").append(row);
			}
		}

		updateTotalPrice();
	}


	function sanitizeId(str) {
		return str.trim().toLowerCase().replace(/\s+/g, '-').replace(/[^\w\-]/g, '');
	}

	function updateTotalPrice() {
		let total = 0;
		$("span[id$='-price']").each(function () {
			const rawText = $(this).text().trim();
			const price = parseFloat(rawText.replace(/[^\d.]/g, ''));
			if (!isNaN(price)) {
				total += price;
			}
		});
		const formattedTotal = "₹" + total.toFixed(2);
		$("#totalPrice, #printTotal").text(formattedTotal);
	}

});


// Save Bill
$(document).on("click", "#billSave", function() {
	
	const productRows = $('#TableDiv tr[id^="row-"]');
	if (productRows.length === 0) {
	    Swal.fire({
	        title: 'No Products Added!',
	        text: 'Please add at least one product before saving the bill.',
	        icon: 'warning',
	        timer: 2000,
	        showConfirmButton: false
	    });
	    return false;
	}

	
	var flag;
	if($('#orderType').val() == 'S' || $('#orderType').val() == null)
	{
		$('#orderTypeSpan').text("Order Type is Mandatory")
		flag =  false;
	}
	else
	{
		$('#orderTypeSpan').text("")
		flag = true;	
	}
	
	if($('#paymentType').val() == 'S' || $('#paymentType').val() == null)
		{
			$('#paymentTypeSpan').text("Payment Type is Mandatory")
			flag =  false;
		}else{
			$('#paymentTypeSpan').text("")
			flag = true;
			
		}
		var position = $('#totalspan').offset();
		$('#amount').css('bottom' , position.bottom);
		/*if(!flag){
			$('#amount').css('top' , '508px');
			return false;
		}else{
			$('#amount').css('top' , '464px')
			
		}*/
	
	const customerInfo = {};
	$('#TableDiv input[id]').each(function() {
		customerInfo[$(this).attr('id')] = $(this).val().trim();
	});
	
		$('#TableDiv select[id]').each(function() {
			customerInfo[$(this).attr('id')] = $(this).val().trim();
		});

		const products = [];
		$('#TableDiv tr').each(function() {
			const product = {};
			$(this).find('td span[id]').each(function() {
				const id = $(this).attr('id').trim();
				const value = $(this).text().trim();
				if (id.endsWith('qty')) product['quantity'] = value;
				else if (id.endsWith('price')) product['price'] = value;
				else if (id.endsWith('product')) product['product'] = value;
				else if (id.endsWith('category')) product['category'] = value;
				else if (id.endsWith('subcat')) product['subCategory'] = value;
			});
			if (Object.keys(product).length > 0) products.push(product);
		});

	const payload = { customerInfo, products };
	$.ajax({
		url: '/save/order',
		type: 'POST',
		contentType: 'application/json',
		data: JSON.stringify(payload),
		success: function(res)
		 { 
			Swal.fire({
			    title: 'Success!',
			    text: 'Order Saved Successfully!',
			    icon: 'success',
			    timer: 1500,
			    showConfirmButton: false
			});
			isBillSaved = true;
		},
		error: function(xhr, status, error) { console.error('Save failed:', error); }
	});
});

// Print Bill
$(document).on("click", "#printBill", function() {
	
	if(!isBillSaved)
	{
		Swal.fire({
	        title: 'Save The Order First',
	        text: 'Please save the Order.',
	        icon: 'warning',
	        timer: 2000,
	        showConfirmButton: false
	    });
	    return false;
	}
	const productRows = $('#TableDiv tr[id^="row-"]');
		if (productRows.length === 0) {
		    Swal.fire({
		        title: 'No Products Added!',
		        text: 'Please add at least one product before saving the bill.',
		        icon: 'warning',
		        timer: 2000,
		        showConfirmButton: false
		    });
		    return false;
		}
	
	const products = [];
	$('#TableDiv tr').each(function() {
		const rowData = {};
		$(this).find('td span[id]').each(function() {
			const id = $(this).attr('id')?.trim();
			const val = $(this).text().trim();

			if (id.endsWith('subcat')) {
				rowData['subCategory'] = val;
			} else if (id.endsWith('category')) {
				rowData['category'] = val;
			} else if (id.endsWith('product')) {
				rowData['product'] = val;
			} else if (id.endsWith('qty')) {
				rowData['quantity'] = val;
			} else if (id.endsWith('price')) {
				rowData['price'] = val;
			}
		});

		if (Object.keys(rowData).length > 0) {
			products.push(rowData);
		}
	});
	const total = $('#printTotal').text().replace('₹', '').trim();
	$.ajax({
		url: '/print/bill',
		type: 'POST',
		contentType: 'application/json',
		data: JSON.stringify({ products, total }),
		xhrFields: { responseType: 'blob' },
		success: function(blob) {
			const file = new Blob([blob], { type: 'application/pdf' });
			const fileURL = URL.createObjectURL(file);
			window.open(fileURL);
		},
		error: function(xhr, status, error) {
			console.error('Preview failed:', error);
		}
	});
});

function toggleSidebar() {
    const sidebar = document.getElementById("sidebarOverlay");
    const backdrop = document.getElementById("overlayBackdrop");
    const toggleBtn = document.getElementById("toggleBtn");

    sidebar.classList.toggle("show");
    backdrop.classList.toggle("show");

    // Hide toggle button when sidebar is open
    toggleBtn.style.display = sidebar.classList.contains("show") ? "none" : "block";
	}
	
	$(document).on("click", ".clear-btn", function() {	
	    location.reload();
	});
	
	$('.nav-links a').click(function (e) {
	    const rowCount = $('#TableDiv tr').length;

	    if (rowCount > 0) {
	        e.preventDefault();
	        const targetHref = $(this).attr('href');

	        Swal.fire({
	            title: 'Are you sure?',
	            text: 'Order is not saved. Do you want to leave this page?',
	            icon: 'warning',
	            showCancelButton: true,
	            allowOutsideClick: false,
	            backdrop: true,
	            confirmButtonText: 'Yes, leave',
	            cancelButtonText: 'No, stay'
	        }).then((result) => {
	            if (result.isConfirmed) {
	                window.location.href = targetHref;
					
	            } else {
					toggleSidebar()
	                $('body').removeClass('swal2-shown');
	                $('html').removeClass('swal2-shown');
	                $('.swal2-container').remove();
	                $('.swal2-backdrop-show').remove();
	            }
	        });
	    }
	});

	
$(document).ready(function(){
	
	if($('#userType').val() == "A")
	{
		$('#toggleBtn').show();
		isAdmin = true;
	}else{
		$('#toggleBtn').hide();
	}	
	
	$("#billTypeDiv").show();
	$('#billTypeDiv').addClass('active');
	
	if(isAdmin)
	{
		$('#toggleBtn').css({
		    'display': 'block',    
		    'opacity': '0'      
		});
	}
	
});

$('.billType').on('click', function(){
	billingType = $(this).data('code');
	$('#billTypeDiv').removeClass('active');
	if(isAdmin)
	{
		$('#toggleBtn').css({
		    'opacity': '1',
		    'filter': 'none'
		});	
	}
	
	if(billingType == 'dine')
	{
		$('#orderType').val('Dine-In').attr('disabled', true);
		}else if(billingType == 'del'){
		$('#orderType').val('Delivery').attr('disabled', true);
	}
});

	