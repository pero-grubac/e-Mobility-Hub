import { UtilService } from './util.service';

describe('UtilService', () => {
  const service = new UtilService();

  it('formats a date as a zero-padded local ISO timestamp', () => {
    const date = new Date(2024, 0, 5, 7, 3, 9);
    expect(service.formatDate(date)).toBe('2024-01-05T07:03:09');
  });

  it('keeps two-digit parts unchanged', () => {
    const date = new Date(2024, 11, 31, 23, 59, 58);
    expect(service.formatDate(date)).toBe('2024-12-31T23:59:58');
  });
});
