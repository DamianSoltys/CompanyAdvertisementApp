import { trigger, state, style, animate, transition, query, animateChild, group } from '@angular/animations';

export const slideInAnimation = trigger('routeAnimations', [
  transition('* <=> *', [
    query(
      ':enter, :leave',
      [
        style({
          position: 'absolute',
          width: '100%',
          opacity: 0,

        }),
        animateChild()
      ],
      { optional: true }
    ),
    query(
      ':enter',
      [
        animate(
          '1s ease',
          style({
            opacity: 1,

          })
        ),
        animateChild()
      ],
      { optional: true }
    )
  ])
]);
