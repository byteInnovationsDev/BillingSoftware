$(document).ready(function() {
  const $reportType = $("#reportType");
  const $userIdDiv = $("#userIdDiv");
  const $userId = $("#ReportUserId");

  // Initially hide user div
  $userIdDiv.hide();

  // Show/hide user div on report type change
  $reportType.change(function() {
    if ($(this).val() === "User Login Report") {
      $userIdDiv.show();
    } else {
      $userIdDiv.hide();
    }
  });

  // On generate report button click
  $(document).on("click", "#genreport", function() {
    const fromdate = $("#FromDate").val();
    let Todate = $("#ToDate").val();
    const reportType = $reportType.val();
    let userIdParam = "";

    if (!reportType) {
      alert("Please select report type");
      return;
    }

    if (!fromdate) {
      alert("Please select from date");
      return;
    }

    if (!Todate) {
      const today = new Date();
      const year = today.getFullYear();
      const month = ("0" + (today.getMonth() + 1)).slice(-2);
      const day = ("0" + today.getDate()).slice(-2);
      Todate = `${year}-${month}-${day}`;
    }

    if (reportType === "User Login Report") {
      const selectedUserId = $userId.val();
      if (!selectedUserId) {
        alert("Please select user");
        return;
      }
      userIdParam = `&userId=${encodeURIComponent(selectedUserId)}`;
    }

    const url = `/genReports?reporttype=${encodeURIComponent(reportType)}&fromdate=${encodeURIComponent(fromdate)}&todate=${encodeURIComponent(Todate)}${userIdParam}`;

    fetch(url)
      .then(response => response.blob())
      .then(blob => {
        const url = window.URL.createObjectURL(blob);
        const a = document.createElement("a");
        a.href = url;
        a.download = "report.xlsx";
        document.body.appendChild(a);
        a.click();
        a.remove();
      })
      .catch(() => alert("Something went wrong."));
  });
});
