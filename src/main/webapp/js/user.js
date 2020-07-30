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
    let newItem = document.getElementById(this.id + 'autocomplete-list');
    if (newItem) {
      newItem = newItem.getElementsByTagName('div');
    }
    if (e.keyCode == 40) {
      // If DOWN key is pressed.
      currentFocus++;
      addActive(newItem);
    } else if (e.keyCode == 38) {
      // If UP key is pressed.
      currentFocus--;
      addActive(newItem);
    } else if (e.keyCode == 13) {
      // If ENTER key is pressed.
      e.preventDefault();
      if (currentFocus > -1) {
        if (newItem) {
          newItem[currentFocus].click();
        }
      }
    }
  });

  function addActive(items) {
    if (!items) {
      return false;
    }
    removeActive(items);
    if (currentFocus >= items.length) {
      currentFocus = 0;
    }
    if (currentFocus < 0) {
      currentFocus = (items.length - 1);
    }
    items[currentFocus].classList.add('autocomplete-active');
  }

  function removeActive(items) {
    for (let i = 0; i < items.length; i++) {
      items[i].classList.remove('autocomplete-active');
    }
  }

  function closeAllLists(elmnt) {
    const items = document.getElementsByClassName('autocomplete-items');
    for (let i = 0; i < items.length; i++) {
      if (elmnt != items[i] && elmnt != input) {
        items[i].parentNode.removeChild(items[i]);
      }
    }
  }

  /* Execute a function when someone clicks in the document */
  document.addEventListener('click', function(e) {
    closeAllLists(e.target);
  });
}
