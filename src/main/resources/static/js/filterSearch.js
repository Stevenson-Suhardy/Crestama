function companyTypeFilterFunction() {
    let input, filter, a, i;
    input = document.getElementById("searchCompanyType");
    filter = input.value.toUpperCase();
    let div = document.getElementById("companyTypeDropdown");
    a = div.getElementsByTagName("a");
    for (i = 0; i < a.length; i++) {
        let txtValue = a[i].textContent || a[i].innerText;
        if (txtValue.toUpperCase().indexOf(filter) > -1) {
            a[i].style.display = "";
        } else {
            a[i].style.display = "none";
        }
    }
}

function cityFilterFunction() {
    let input, filter, a, i;
    input = document.getElementById("searchCity");
    filter = input.value.toUpperCase();
    let div = document.getElementById("cityDropdown");
    a = div.getElementsByTagName("a");
    for (i = 0; i < a.length; i++) {
        let txtValue = a[i].textContent || a[i].innerText;
        if (txtValue.toUpperCase().indexOf(filter) > -1) {
            a[i].style.display = "";
        } else {
            a[i].style.display = "none";
        }
    }
}

function showType(item) {
    document.getElementById("dropdownMenuButton").innerHTML = item.innerHTML;
}

function showCity(item) {
    document.getElementById("cityDropdownMenuButton").innerHTML = item.innerHTML;
}