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
    WOMEN: 'Women',
    SKIING: 'Skiing',
    BACKPACKING: 'Backpacking',
    TRAVELING: 'Traveling',
    HIKING: 'Hiking',
    CAMPING: 'Camping',
    KAYAKING: 'Kayaking',
    CLIMBING: 'Climbing',
    BEGINNER: 'Beginner',
    INTERMEDIATE: 'Intermediate',
    ADVANCED: 'Advanced',
    EXPERT: 'Expert',
  };
  return map[val] || (val ? val.charAt(0).toUpperCase() + val.slice(1).toLowerCase() : 'N/A');
};
