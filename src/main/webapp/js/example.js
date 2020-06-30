// This is an example JavaScript file to test linting.
// Feel free to delete it once actual JS is committed to the repo.

/**
 * Computes the factorial of the input.
 *
 * The input is assumed to be a nonnegative integer.
 */
const factorial = (n) => {
  let result = 1;
  while (n > 1) {
    result *= n;
    n -= 1;
  }
  return result;
};

/** Returns a Promise resolving to 2. */
const gimmeTwo = async () => {
  return 2;
};

/** Returns a Promise resolving to 2, but in a dumb way. */
const convolutedGimmeTwo = async () => {
  return await gimmeTwo();
};

/** Returns 2, but with stupid side effects. */
const weirdFlexButStillGivesMeTwo = () => {
  convolutedGimmeTwo().then((value) => {
    console.assert(value === 2, 'I didn\'t get 2?');
  });
  return 2;
};

class Foo {
  constructor() {
    this.two = weirdFlexButStillGivesMeTwo();
  }

  bar() {
    console.log('baz');
    return factorial(this.two);
  }
}

console.log(new Foo().bar());
