export const formatEnum = (val) => {
  const map = {
    MALE: 'Male',
    FEMALE: 'Female',
    NONBINARY: 'Non-binary',
    PREFERNOT: 'Prefer not to say',
    FRIENDSHIPS: 'Friendships',
    RELATIONSHIPS: 'Relationships',
    BOTH: 'Both',
    MEN: 'Men',
    WOMEN: 'Women'
  };
  return map[val] || (val ? val.charAt(0).toUpperCase() + val.slice(1).toLowerCase() : 'N/A');
};
