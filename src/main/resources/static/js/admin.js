const csrfToken =
document.querySelector("meta[name='_csrf']").content;

const csrfHeader =
document.querySelector("meta[name='_csrf_header']").content;

const dates = [];

const addDateBtn = document.getElementById("addDate");
const selectedDates = document.getElementById("selectedDates");
const saveBtn = document.getElementById("saveAvailability");
const cancelBtn = document.getElementById("cancelEdit");
const table = document.getElementById("availabilityTable");

const availabilityId = document.getElementById("availabilityId");

const doctor = document.getElementById("doctorName");
const availableDate = document.getElementById("availableDate");
const startTime = document.getElementById("startTime");
const endTime = document.getElementById("endTime");
const available = document.getElementById("available");
function showToast(message, success = true){

    const toast = document.getElementById("toast");
    const text = document.getElementById("toastText");

    if(!toast || !text){
        console.error("Toast element not found");
        return;
    }

    text.textContent = message;

    toast.classList.remove("toast-success","toast-error","show");

    if(success){
        toast.classList.add("toast-success");
    }else{
        toast.classList.add("toast-error");
    }

    toast.classList.add("show");

    setTimeout(function(){
        toast.classList.remove("show");
    },3000);

}

loadAvailability();

addDateBtn.addEventListener("click", function () {

    if (availabilityId.value !== "") {

       showToast("While editing you can edit only one date.", false);

        return;

    }

    const date = availableDate.value;

    if (date === "")
        return;

    if (!dates.includes(date)) {

        dates.push(date);

        renderDates();

    }

});

function renderDates() {

    selectedDates.innerHTML = "";

    dates.forEach(function (d, index) {

        selectedDates.innerHTML +=

        `<div style="margin:6px 0;">

            ${d}

            <button
                type="button"
                onclick="removeDate(${index})">

                Remove

            </button>

        </div>`;

    });

}

window.removeDate = function(index){

    dates.splice(index,1);

    renderDates();

}

saveBtn.addEventListener("click", function () {

    if (availabilityId.value !== "") {

        updateAvailability();

        return;

    }

    if (dates.length === 0) {

        showToast("Select at least one date.", false);

        return;

    }

    const params = new URLSearchParams();

    params.append("doctor", doctor.value);

    dates.forEach(function(d){

        params.append("dates", d);

    });

    params.append("startTime", startTime.value);

    params.append("endTime", endTime.value);

    params.append("available", available.checked);

    fetch("/admin/doctor/save",{

        method:"POST",

		headers:{
		    "Content-Type":"application/x-www-form-urlencoded",
		    [csrfHeader]:csrfToken
		},

        body:params

    })

    .then(r=>r.text())

    .then(msg=>{

       showToast(msg, true);

        clearForm();

        loadAvailability();

    });

});

function updateAvailability(){

    const params = new URLSearchParams();

    params.append("id", availabilityId.value);

    params.append("doctor", doctor.value);

    params.append("date", availableDate.value);

    params.append("startTime", startTime.value);

    params.append("endTime", endTime.value);

    params.append("available", available.checked);

    fetch("/admin/doctor/update",{

        method:"POST",

		headers:{
		    "Content-Type":"application/x-www-form-urlencoded",
		    [csrfHeader]:csrfToken
		},

        body:params

    })

    .then(r=>r.text())

    .then(msg=>{

        alert(msg);

        clearForm();

        loadAvailability();

    });

}

cancelBtn.addEventListener("click", function(){

    clearForm();

});

function clearForm(){

    availabilityId.value="";

    doctor.selectedIndex=0;

    availableDate.value="";

    startTime.value="";

    endTime.value="";

    available.checked=true;

    dates.length=0;

    renderDates();

    saveBtn.textContent="Save Availability";

    cancelBtn.style.display="none";

}
function loadAvailability() {

    fetch("/admin/doctor/all")

    .then(r => r.json())

    .then(data => {

        table.innerHTML = "";

        data.forEach(item => {

            table.innerHTML += `

            <tr>

                <td>${item.doctorName}</td>

                <td>${item.availableDate}</td>

                <td>${formatTime(item.startTime)} - ${formatTime(item.endTime)}</td>

                <td>${item.available ? "Available" : "Not Available"}</td>

                <td>

                    <button
                        type="button"
                        onclick="editAvailability(${item.id})">

                        Edit

                    </button>

                    <button
                        type="button"
                        onclick="deleteAvailability(${item.id})">

                        Delete

                    </button>

                </td>

            </tr>

            `;

        });

    });

}

window.editAvailability = function(id){

    fetch("/admin/doctor/" + id)

    .then(r => r.json())

    .then(item => {

        availabilityId.value = item.id;

        doctor.value = item.doctorName;

        availableDate.value = item.availableDate;

        startTime.value = item.startTime.substring(0,5);

        endTime.value = item.endTime.substring(0,5);

        available.checked = item.available;

        dates.length = 0;

        renderDates();

        saveBtn.textContent = "Update Availability";

        cancelBtn.style.display = "inline-block";

        window.scrollTo({

            top: document.getElementById("doctorAvailability").offsetTop,

            behavior: "smooth"

        });

    });

}

window.deleteAvailability = function(id){

    if(!confirm("Delete this availability?")){

        return;

    }

	fetch("/admin/doctor/delete/" + id,{

	    method:"DELETE",

	    headers:{
	        [csrfHeader]:csrfToken
	    }

	})

    .then(()=>{

        loadAvailability();

    });

}

function formatTime(time){

    let parts = time.split(":");

    let hour = parseInt(parts[0]);

    let minute = parts[1];

    let ampm = "AM";

    if(hour >= 12){

        ampm = "PM";

    }

    if(hour == 0){

        hour = 12;

    }
    else if(hour > 12){

        hour -= 12;

    }

    return hour.toString().padStart(2,"0")
            + ":"
            + minute
            + " "
            + ampm;

}


const modal = document.getElementById("appointmentModal");
const closeModal = document.getElementById("closeModal");

document.querySelectorAll(".viewAppointment").forEach(function(btn){

    btn.addEventListener("click", function(){

        const id = this.dataset.id;

        fetch("/admin/appointment/" + id)

        .then(r => r.json())

        .then(data => {

            document.getElementById("viewName").textContent =
                    data.patientName;

            document.getElementById("viewEmail").textContent =
                    data.email || "-";

            document.getElementById("viewAge").textContent =
                    data.age;

            document.getElementById("viewGender").textContent =
                    data.gender;

            document.getElementById("viewDepartment").textContent =
                    data.department;

            document.getElementById("viewDoctor").textContent =
                    data.doctor;

            document.getElementById("viewDate").textContent =
                    data.appointmentDate;

            document.getElementById("viewTime").textContent =
                    formatTime(data.timeSlot);

            document.getElementById("viewReason").textContent =
                    data.reason;

            document.getElementById("viewDetails").value =
                    data.additionalDetails || "";

            const mobile =
                document.getElementById("viewMobile");

            mobile.textContent =
                data.mobileNumber;

            mobile.href =
                "tel:" + data.mobileNumber;

            modal.style.display = "block";

        });

    });

});

closeModal.addEventListener("click", function(){

    modal.style.display = "none";

});

window.addEventListener("click", function(e){

    if(e.target === modal){

        modal.style.display = "none";

    }

});
/* ===========================
   APPOINTMENT SEARCH
=========================== */

const appointmentSearch =
    document.getElementById("appointmentSearch");

appointmentSearch.addEventListener("keyup", function () {

    const value = this.value.toLowerCase();

    const rows =
        document.querySelectorAll(".appointmentRow");

    rows.forEach(function (row) {

        if (row.innerText
                .toLowerCase()
                .includes(value)) {

            row.style.display = "";

        } else {

            row.style.display = "none";

        }

    });

});