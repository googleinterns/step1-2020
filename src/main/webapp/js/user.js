function autocomplete(input, languages) {
  let currentFocus;
  input.addEventListener('input', function(e) {
    let autocomplete = this.value;
    let language = this.value;
    const val = this.value;
    closeAllLists();
    if (!val) {
      return false;
    }
    currentFocus = -1;
    autocomplete = document.createElement('div');
    autocomplete.setAttribute('id', this.id + 'autocomplete-list');
    autocomplete.setAttribute('class', 'autocomplete-items');
    this.parentNode.appendChild(autocomplete);
    for (let i = 0; i < languages.length; i++) {
      if (languages[i].substr(0,
          val.length).toUpperCase() == val.toUpperCase()) {
        language = document.createElement('div');
        // Add each element with bolded part for input characters.
        language.innerHTML = '<strong>' + languages[i].substr(0,
            val.length) + '</strong>' + languages[i].substr(val.length);
        language.innerHTML += '<input type="hidden" value="'+
            languages[i] + '">';
        language.addEventListener('click', function(e) {
          input.value = this.getElementsByTagName('input')[0].value;
          closeAllLists();
        });
        autocomplete.appendChild(language);
      }
    }
  });
  /* Execute a function presses a key on the keyboard */
  input.addEventListener('keydown', function(e) {
    let x = document.getElementById(this.id + 'autocomplete-list');
    if (x) {
      x = x.getElementsByTagName('div');
    }
    if (e.keyCode == 40) {
      // If DOWN key is pressed.
      currentFocus++;
      addActive(x);
    } else if (e.keyCode == 38) {
      // If UP key is pressed.
      currentFocus--;
      addActive(x);
    } else if (e.keyCode == 13) {
      // If ENTER key is pressed.
      e.preventDefault();
      if (currentFocus > -1) {
        if (x) x[currentFocus].click();
      }
    }
  });

  function addActive(x) {
    if (!x) return false;
    removeActive(x);
    if (currentFocus >= x.length) currentFocus = 0;
    if (currentFocus < 0) currentFocus = (x.length - 1);
    x[currentFocus].classList.add('autocomplete-active');
  }

  function removeActive(x) {
    for (let i = 0; i < x.length; i++) {
      x[i].classList.remove('autocomplete-active');
    }
  }

  function closeAllLists(elmnt) {
    const x = document.getElementsByClassName('autocomplete-items');
    for (let i = 0; i < x.length; i++) {
      if (elmnt != x[i] && elmnt != input) {
        x[i].parentNode.removeChild(x[i]);
      }
    }
  }

  /* Execute a function when someone clicks in the document */
  document.addEventListener('click', function (e) {
    closeAllLists(e.target);
  });
}
