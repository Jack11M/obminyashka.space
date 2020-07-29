$(function() {
    // category-select
    // Set
    var main = $('div.dropdown-category .textfirst')
    var li = $('div.dropdown-category > ul > li.input-option')
    var inputoption = $("div.dropdown-category .option")
    var default_text = 'Выбрать<img src="../../../images/arrow-selection.svg" width="10" height="10" class="down" />';
  
    // Animation
    main.click(function() {
      main.html(default_text);
      li.toggle('fast');
    });
  
    // Insert Data
    li.click(function() {
      // hide
      li.toggle('fast');
      var livalue = $(this).data('value');
      var lihtml = $(this).html();
      main.html(lihtml);
      inputoption.val(livalue);
    });

     // subcategory-select
    // Set
    var main2 = $('div.dropdown-subcategory .textfirst')
    var li2 = $('div.dropdown-subcategory > ul > li.input-option')
    var inputoption2 = $("div.dropdown-subcategory .option")
    var default_text2 = 'Выбрать<img src="../../../images/arrow-selection.svg" width="10" height="10" class="down" />';
  
    // Animation
    main2.click(function() {
      main2.html(default_text2);
      li2.toggle('fast');
    });
  
    // Insert Data
    li2.click(function() {
      // hide
      li2.toggle('fast');
      var livalue2 = $(this).data('value');
      var lihtml2 = $(this).html();
      main2.html(lihtml2);
      inputoption2.val(livalue2);
    });
  });