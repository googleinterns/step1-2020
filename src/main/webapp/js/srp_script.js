var card = document.getElementById("card");
card.onscroll = function() {
  console.log("hello");
  stickyHeader()
};

var header = document.getElementById("card-header");
var sticky = header.offsetTop;

function stickyHeader() {
  if (card.pageYOffset > sticky) {
    header.classList.add("sticky");
  } else {
    header.classList.remove("sticky");
  }
}