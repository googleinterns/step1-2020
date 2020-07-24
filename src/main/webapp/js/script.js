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
  const total = card.getElementsByClassName('total-votes')[0];
  const jsonElem = await updateVote(url, action);
  if (Object.keys(jsonElem).length !== 0) {
    total.innerHTML = jsonElem.totalVotes;
    const upButton = card.getElementsByClassName('upvote')[0];
    const downButton = card.getElementsByClassName('downvote')[0];
    if (jsonElem.toggleUpvote === 'true') {
      upButton.style.color = 'green';
    } else {
      upButton.style.color = 'black';
    }
    if (jsonElem.toggleDownvote === 'true') {
      downButton.style.color = 'red';
    } else {
      downButton.style.color = 'black';
    }
  } else {
    alert('You are not currently signed in. Please sign in to add feedback.');
  }
}

async function updateVote(url, action) {
  const response = await fetch('/vote', {
    method: 'POST',
    body: 'url=' + url + '&' + action,
    headers: {
      'Content-Type': 'application/x-www-form-urlencoded',
    },
  });
  return response.json();
}
