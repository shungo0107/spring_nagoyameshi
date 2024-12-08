 let maxDate = new Date();
 maxDate = maxDate.setMonth(maxDate.getMonth() + 3);
 
 flatpickr('#ReserveDate', {
   mode: "single",
   locale: 'ja',
   minDate: 'today',
   maxDate: maxDate
 });