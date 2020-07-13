var searchButtonElements = document.getElementsByClassName('gsc-search-button');
console.log(searchButtonElements);

function getCardJson() {
  fetch(`/data?comment-choice=${displayValue}`).then(response => response.json()).then((cards) => {
    console.log(cards);
  });
}