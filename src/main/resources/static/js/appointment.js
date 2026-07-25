document.addEventListener("DOMContentLoaded", function () {

    const department = document.getElementById("department");
    const doctor = document.getElementById("doctor");
    const reason = document.getElementById("reason");
    const appointmentDate = document.getElementById("appointmentDate");
    const timeSlot = document.getElementById("timeSlot");
	const mobileNumber = document.getElementById("mobileNumber");

    let availableDates = [];

    let picker = flatpickr(appointmentDate, {
        dateFormat: "Y-m-d",
        minDate: "today",
        enable: [],
        disableMobile: true
    });

    /* Department Changed */
    department.addEventListener("change", function () {

        loadDoctors(this.value);
        loadReasons(this.value);

        doctor.innerHTML =
            '<option value="">Select Doctor</option>';

        timeSlot.innerHTML =
            '<option value="">Select Time</option>';

        picker.clear();
        picker.set("enable", []);

    });

    /* Doctor Changed */
    doctor.addEventListener("change", function () {

        picker.clear();

        timeSlot.innerHTML =
            '<option value="">Select Time</option>';

        if (this.value === "")
            return;

        loadAvailableDates(this.value);

    });

    /* Date Changed */
    appointmentDate.addEventListener("change", function () {

        loadTimeSlots();

    });

    function loadDoctors(departmentValue) {

        doctor.innerHTML =
            '<option value="">Select Doctor</option>';

        if (departmentValue === "")
            return;

        fetch("/appointment/doctors?department=" + departmentValue)

            .then(r => r.json())

            .then(data => {

                data.forEach(function (item) {

                    let option =
                        document.createElement("option");

                    option.value = item;
                    option.textContent = item;

                    doctor.appendChild(option);

                });

            });

    }

    function loadReasons(departmentValue) {

        reason.innerHTML =
            '<option value="">Select Reason</option>';

        if (departmentValue === "")
            return;

        fetch("/appointment/reasons?department=" + departmentValue)

            .then(r => r.json())

            .then(data => {

                data.forEach(function (item) {

                    let option =
                        document.createElement("option");

                    option.value = item;
                    option.textContent = item;

                    reason.appendChild(option);

                });

            });

    }

    function loadAvailableDates(doctorName) {

        fetch("/appointment/available-dates?doctor="
                + encodeURIComponent(doctorName))

            .then(r => r.json())

            .then(data => {

                availableDates = data;

                picker.set("enable", availableDates);

            });

    }

    function loadTimeSlots() {

        if (doctor.value === "")
            return;

        if (appointmentDate.value === "")
            return;

        timeSlot.innerHTML =
            '<option value="">Loading...</option>';

			fetch("/appointment/time-slots?doctor="
			        + encodeURIComponent(doctor.value)
			        + "&date="
			        + picker.input.value)

            .then(r => r.json())

            .then(data => {

                timeSlot.innerHTML =
                    '<option value="">Select Time</option>';

                data.forEach(function (slot) {

                    let option =
                        document.createElement("option");

						option.value = slot;
						option.textContent = formatTime(slot);

						timeSlot.appendChild(option);

                });

            });

    }

	mobileNumber.addEventListener("input", function () {

	    this.value = this.value.replace(/\D/g, "");

	    if (this.value.length > 10) {

	        this.value = this.value.substring(0, 10);

	    }

	});
	function formatTime(time) {

	    let parts = time.split(":");

	    let hour = parseInt(parts[0]);

	    let minute = parts[1];

	    let ampm = "AM";

	    if (hour >= 12) {

	        ampm = "PM";

	    }

	    if (hour === 0) {

	        hour = 12;

	    } else if (hour > 12) {

	        hour -= 12;

	    }

	    return hour.toString().padStart(2, "0")
	            + ":"
	            + minute
	            + " "
	            + ampm;

	}
});

