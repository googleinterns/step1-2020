window.addEventListener('DOMContentLoaded', (event) => {
  const cards = document.getElementsByClassName('card');
  for (const card of cards) {
    const up = card.getElementsByClassName('upvote')[0];
    up.addEventListener('click', function() {
      renderVote(card, up.value, 'upvote');
    });
    const down = card.getElementsByClassName('downvote')[0];
    down.addEventListener('click', function() {
      renderVote(card, down.value, 'downvote');
    });
  }
});

async function renderVote(card, url, action) {
  const total = card.getElementsByClassName('totalvote')[0];
  total.innerHTML = await updateVote(url, action);
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
