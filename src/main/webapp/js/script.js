window.addEventListener('DOMContentLoaded', (event) => {
  const downVotes = document.getElementsByClassName('downvote');
  for (const down of downVotes) {
    down.addEventListener('click', function() {
      renderVote(down, 'downvote');
    });
  }
  const upVotes = document.getElementsByClassName('upvote');
  for (const up of upVotes) {
    up.addEventListener('click', function() {
      renderVote(up, 'upvote');
    });
  }
});

async function renderVote(elem, action) {
  elem.innerHTML = await updateVote(elem.value, action);
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
