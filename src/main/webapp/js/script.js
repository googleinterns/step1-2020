window.addEventListener('DOMContentLoaded', (event) => {
  const cards = document.getElementsByClassName('card');
  for (const card of cards) {
    const up = card.getElementsByClassName('upvote')[0];
    up.addEventListener('click', function() {
      renderVote(card, 'upvote');
    });
    const down = card.getElementsByClassName('downvote')[0];
    down.addEventListener('click', function() {
      renderVote(card, 'downvote');
    });
  }
});

async function renderVote(card, action) {
  const total = card.getElementsByClassName('total-votes')[0];
  total.innerHTML = await updateVote(card.value, action);
}

async function updateVote(url, action) {
  const response = await fetch('/vote', {
    method: 'POST',
    body: 'url=' + url + '&' + action,
    headers: {
      'Content-Type': 'application/x-www-form-urlencoded',
    },
  });
  return response.text();
}
