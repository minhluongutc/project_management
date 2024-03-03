import { DatePipe } from '@angular/common';

/**
 * Converts a number representing a date into a formatted date string.
 * @param {number | null} date - The `date` parameter in the `transformDate` function is a number or
 * `null`. If the `date` is a number, it represents a timestamp value.
 * @returns A date string in the format "dd/MM/yyyy".
 */
export const transformDate = (date: number | null) =>
  date ? new DatePipe('en-US').transform(date, 'dd/MM/yyyy') : null;

/**
 * Converts a date string into a `Date` object.
 * @param {string} dateString - The `dateString` parameter is a string representing a date in the format "dd/MM/yyyy".
 * @returns A `Date` object.
 */
export const dateStringToDate = (dateString: string) => {
  if (!dateString) {
    return dateString;
  }

  // Rearrange the date string to a parsable format (mm/dd/yyyy or yyyy/mm/dd)
  const parsedDateString = dateString.split('/').reverse().join('/');

  return new Date(Date.parse(parsedDateString));
};
