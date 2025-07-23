function generateBill() {
    const orderData = {
        productName: $("#productName").val(),
        quantity: parseInt($("#quantity").val()),
        price: parseFloat($("#price").val()),
        paymentType: $("#paymentType").val(),
        orderType: $("#orderType").val(),
        customerName: $("#customerName").val(),
        customerPhoneNumber: $("#customerPhone").val()
    };
    
    $.ajax({
        url: '/api/orders',
        type: 'POST',
        contentType: 'application/json',
        data: JSON.stringify(orderData),
        success: function(response) {
            // Get the text bill
            $.get('/api/orders/' + response.orderId + '/text', function(billText) {
                $('#billOutput').text(billText);
                
                // In a real application, you would send this to the printer
                console.log("Bill ready for printing:");
                console.log(billText);
            });
        },
        error: function(xhr) {
            alert("Error generating bill: " + xhr.responseText);
        }
    });
}