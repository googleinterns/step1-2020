window.addEventListener('DOMContentLoaded', (event) => {
  const cards = document.getElementsByClassName('card');
  initVote(cards);
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

function initVote(cards) {
  for (const card of cards) {
    const link = card.querySelector('#link');
    fetch('/vote?url=' + link.href).then((res) => res.json()).then((json) => {
      if (Object.keys(json).length !== 0) {
        const upButton = card.getElementsByClassName('upvote')[0];
        const downButton = card.getElementsByClassName('downvote')[0];
        if (json.toggleUpvote === 'active') {
          upButton.style.color = 'green';
        } else {
          upButton.style.color = 'black';
        }
        if (json.toggleDownvote === 'active') {
          downButton.style.color = 'red';
        } else {
          downButton.style.color = 'black';
        }
      }
    });
  }
}

async function renderVote(card, url, action) {
  const total = card.getElementsByClassName('total-votes')[0];
  const json = await updateVote(url, action);
  if (Object.keys(json).length !== 0) {
    total.innerHTML = json.totalvotes;
    const upButton = card.getElementsByClassName('upvote')[0];
    const downButton = card.getElementsByClassName('downvote')[0];
    if (json.toggleUpvote === 'active') {
      upButton.style.color = 'green';
    } else {
      upButton.style.color = 'black';
    }
    if (json.toggleDownvote === 'active') {
      downButton.style.color = 'red';
    } else {
      downButton.style.color = 'black';
    }
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
