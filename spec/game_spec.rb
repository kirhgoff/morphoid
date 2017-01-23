require './lib/game'
require './lib/creature'

describe Morphoid::Game do
  let(:game) { Morphoid::Game.new(10, 10) }

  it "should fix coords properly" do
    expect(check_fix(1,1)).to eq [1,1]
    expect(check_fix(-1,1)).to eq [0,1]
    expect(check_fix(1,-15)).to eq [1,0]
    expect(check_fix(10,5)).to eq [9,5]
    expect(check_fix(15,5)).to eq [9,5]
    expect(check_fix(15,25)).to eq [9,9]
  end

  def check_fix(x, y)
    object = Morphoid::Creature.new(x, y)
    game.fix_object(object)
    [object.x, object.y]
  end
end
